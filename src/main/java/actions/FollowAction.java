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
import constants.MessageConst;
import models.Employee;
import services.EmployeeService;
import services.FollowService;
import services.ReportService;

public class FollowAction extends ActionBase {
	private FollowService service;
	private EmployeeService employeeService;
	private ReportService reportService;

	@Override
	public void process() throws ServletException, IOException {
		service = new FollowService();
		employeeService = new EmployeeService();
		reportService = new ReportService();
		//		メソッドを実行
		invoke();
		service.close();
		employeeService.close();
		reportService.close();
	}

	//	一覧画面を表示する
	public void index() throws ServletException, IOException {
		//		指定されたページ数の一覧画面に表示するデータを取得
		int page = getPage();

		Employee employee_id = EmployeeConverter
				.toModel((EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP));

		List<EmployeeView> employees = employeeService.getPerPage(page);
		List<FollowView> follows = service.getFollows(employee_id);

		for (EmployeeView employee : employees) {
			for (FollowView follow : follows) {
				//				フォローボタンをクリックした従業員 かつ フォローフラグが 0 の時
				if (employee.getId() == follow.getFollow_id().getId()) {
					//		EmployeeViewに新設したフォローフラグをtrueにする
					employee.setIsFollow(true);
					break;
				}
			}
		}

		//		全ての従業員データの件数を取得
		long employeeCount = service.countAll();

		putSessionScope(AttributeConst.FOLLOWS, follows); // 取得したフォローされる従業員データ

		putRequestScope(AttributeConst.EMPLOYEES, employees); // 取得した従業員データ
		putRequestScope(AttributeConst.EMP_COUNT, employeeCount); //全ての従業員データの件数
		putRequestScope(AttributeConst.PAGE, page); // ページ数
		putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); // 1ページに表示するレコードの数

		String flush = getSessionScope(AttributeConst.FLUSH);

		if (flush != null) {
			putRequestScope(AttributeConst.FLUSH, flush);
			removeSessionScope(AttributeConst.FLUSH);
		}

		//		一覧画面を表示
		forward(ForwardConst.FW_FOL_INDEX);
	}

	//	フォローを行う
	public void follow() throws ServletException, IOException {
		//		セッションからログイン中の従業員情報を取得　
		EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
		//		idを条件にフォローされる従業員情報を取得
		EmployeeView follower = employeeService.findOne(toNumber(getRequestParam(AttributeConst.FOL_ID)));

		//		パラメータの値を元にフォローされる従業員情報のインスタンスを作成する
		FollowView fv = new FollowView(
				null, // Integer id
				ev, // Employee employee_id
				follower, // Employee follow_id
				null, // LocalDateTime created_at
				null, // LocalDateTime updated_at
				AttributeConst.DEL_FLAG_FALSE.getIntegerValue() // Integer delete_flag
		);
		//		登録
		service.create(fv);

		//		フラッシュメッセージをセッションに登録
		putSessionScope(AttributeConst.FLUSH, fv.getFollow_id().getName() + MessageConst.I_FOLLOWED.getMessage());

		redirect(ForwardConst.ACT_FOL, ForwardConst.CMD_INDEX);
	}

	public void destroy() throws ServletException, IOException {
		//		ログイン中の従業員のフォローリストを取得
		List<FollowView> follower = getSessionScope(AttributeConst.FOLLOWS);

		//		フォローリストの中から フォローされる従業員id と送信されたリクエストパラメータ フォロー解除ボタンを押した解除対象の従業員id が同じ場合
		//		destroyメソッドを実行
		for (FollowView follow : follower) {
			if (follow.getFollow_id().getId() == toNumber(getRequestParam(AttributeConst.FOL_ID))) {
				//		論理削除メソッド
				service.destroy(follow.getId());
				//				フラッシュメッセージをセッションに登録
				putSessionScope(AttributeConst.FLUSH,
						follow.getFollow_id().getName() + MessageConst.I_UNFOLLOWED.getMessage());
				break;
			}
		}

		//		一覧画面にリダイレクト
		redirect(ForwardConst.ACT_FOL, ForwardConst.CMD_INDEX);
	}

	public void showFollow() throws ServletException, IOException {
		//		セッションからログイン中の従業員を取得
		EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

		//指定されたページ数の一覧画面に表示する日報データを取得
		int page = getPage();

		//		ログイン中の従業員がフォロー中の従業員の日報リストを取得
		List<ReportView> reports = reportService.getFollowReports(page, ev);

		//全日報データの件数を取得
		long reportsCount = reports.size();
		putRequestScope(AttributeConst.REP_FOL, reports); // 取得したフォロー中の従業員の日報データ
		putRequestScope(AttributeConst.REP_FOL_COUNT, reportsCount); // フォロー中の従業員全ての日報データの件数
		putRequestScope(AttributeConst.PAGE, page); //ページ数
		putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

		forward(ForwardConst.FW_FOL_SHOW_FOLLOW);
	}
}
