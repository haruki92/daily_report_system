package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import constants.PropertyConst;
import models.Employee;
import services.EmployeeService;
import services.FollowService;

public class AuthAction extends ActionBase {
	private EmployeeService service;
	private FollowService followService;

	//	メソッドを実行する
	@Override
	public void process() throws ServletException, IOException {
		service = new EmployeeService();
		followService = new FollowService();

		//		メソッドを実行
		invoke();
		service.close();
		followService.close();
	}

	//	ログイン画面を表示する
	public void showLogin() throws ServletException, IOException {
		//		CSRF対策用トークン
		putRequestScope(AttributeConst.TOKEN, getTokenId());

		//		セッションにフラッシュメッセージが登録されている場合はリクエストスコープに設定する
		String flush = getSessionScope(AttributeConst.FLUSH);
		if (flush != null) {
			putRequestScope(AttributeConst.FLUSH, flush);
			removeSessionScope(AttributeConst.FLUSH);
		}

		//		ログイン画面を表示
		forward(ForwardConst.FW_LOGIN);
	}

	//	ログイン処理を行う
	public void login() throws ServletException, IOException {

		String code = getRequestParam(AttributeConst.EMP_CODE);
		String plainPass = getRequestParam(AttributeConst.EMP_PASS);
		String pepper = getContextScope(PropertyConst.PEPPER);

		//有効な従業員か認証する
		Boolean isValidEmployee = service.validateLogin(code, plainPass, pepper);

		if (isValidEmployee) {
			//認証成功の場合
			//CSRF対策 tokenのチェック
			if (checkToken()) {
				//ログインした従業員のDBデータを取得
				EmployeeView ev = service.findOne(code, plainPass, pepper);

				//セッションにログインした従業員を設定
				putSessionScope(AttributeConst.LOGIN_EMP, ev);

				//		フォローテーブルに登録されている従業員情報でemployee_id=ログイン中の従業員 
				//		かつ follow_flag = 0 に当てはまるデータ
				Employee employee_id = EmployeeConverter
						.toModel((EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP));
				List<FollowView> follows = followService.getFollows(employee_id);

				//		ログイン中の従業員がフォロー中の従業員情報をセッションスコープに登録
				putSessionScope(AttributeConst.FOLLOWS, follows);
				//				フォロー中の件数
				putSessionScope(AttributeConst.FOL_COUNT, follows.size());

				//セッションにログイン完了のフラッシュメッセージを設定
				putSessionScope(AttributeConst.FLUSH, MessageConst.I_LOGINED.getMessage());
				//トップページへリダイレクト
				redirect(ForwardConst.ACT_TOP, ForwardConst.CMD_INDEX);
			}
		} else

		{
			//認証失敗の場合

			//CSRF対策用トークンを設定
			putRequestScope(AttributeConst.TOKEN, getTokenId());
			//認証失敗エラーメッセージ表示フラグをたてる
			putRequestScope(AttributeConst.LOGIN_ERR, true);
			//入力された従業員コードを設定
			putRequestScope(AttributeConst.EMP_CODE, code);

			//ログイン画面を表示
			forward(ForwardConst.FW_LOGIN);
		}
	}

	//	ログアウト処理を行う
	public void logout() throws ServletException, IOException {
		//		セッションからログイン従業員のパラメータを削除
		removeSessionScope(AttributeConst.LOGIN_EMP);

		//		セッションにログアウト時のフラッシュメッセージを設定
		putSessionScope(AttributeConst.FLUSH, MessageConst.I_LOGOUT.getMessage());

		//		ログイン画面にリダイレクト
		redirect(ForwardConst.ACT_AUTH, ForwardConst.CMD_SHOW_LOGIN);
	}
}
