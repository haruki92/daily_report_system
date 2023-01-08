package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import models.Employee;
import services.FollowService;
import services.ReportService;

public class TopAction extends ActionBase {
	private ReportService reportService;
	private FollowService followService;

	@Override
	public void process() throws ServletException, IOException {
		reportService = new ReportService();
		followService = new FollowService();
		//		メソッドを実行
		invoke();

		reportService.close();
		followService.close();
	}

	//	一覧画面を表示する
	public void index() throws ServletException, IOException {

		//セッションからログイン中の従業員情報を取得
		EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

		//ログイン中の従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得する
		int page = getPage();
		List<ReportView> reports = reportService.getMinePerPage(loginEmployee, page);

		//ログイン中の従業員が作成した日報データの件数を取得
		long myReportsCount = reportService.countAllMine(loginEmployee);

		//		フォローテーブルに登録されている従業員情報でemployee_id=ログイン中の従業員 
		//		かつ follow_flag = 0 に当てはまるデータ
		Employee employee_id = EmployeeConverter
				.toModel((EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP));
		List<FollowView> follows = followService.getFollows(employee_id);

		//		ログイン中の従業員がフォロー中の従業員情報をセッションスコープに登録
		putSessionScope(AttributeConst.FOLLOWS, follows);
		//				フォロー中の件数
		putSessionScope(AttributeConst.FOL_COUNT, follows.size());

		putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
		putRequestScope(AttributeConst.REP_COUNT, myReportsCount); //ログイン中の従業員が作成した日報の数
		putRequestScope(AttributeConst.PAGE, page); //ページ数
		putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

		//		セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、
		//		セッションからは削除する
		String flush = getSessionScope(AttributeConst.FLUSH);

		if (flush != null) {
			putRequestScope(AttributeConst.FLUSH, flush);
			removeSessionScope(AttributeConst.FLUSH);
		}

		//		一覧画面を表示
		forward(ForwardConst.FW_TOP_INDEX);
	}

}
