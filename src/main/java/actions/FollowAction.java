package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.EmployeeService;
import services.FollowService;

public class FollowAction extends ActionBase {
	private FollowService service;
	private EmployeeService employeeService;

	@Override
	public void process() throws ServletException, IOException {
		service = new FollowService();
		employeeService = new EmployeeService();

		//		メソッドを実行
		invoke();
		service.close();
	}

	//	一覧画面を表示する
	public void index() throws ServletException, IOException {
		//		指定されたページ数の一覧画面に表示するデータを取得
		int page = getPage();
		List<FollowView> follows = service.getFollows(page);
		List<EmployeeView> employees = service.getPerPage(page);

		//		全ての従業員データの件数を取得
		long followCount = service.getFollowCount();
		long employeeCount = service.countAll();

		putRequestScope(AttributeConst.EMPLOYEES, employees); //取得した従業員データ
		putRequestScope(AttributeConst.EMP_COUNT, employeeCount); //全ての従業員データの件数
		putRequestScope(AttributeConst.FOLLOWS, follows); // 取得したフォロー中の従業員データ
		putRequestScope(AttributeConst.FOL_COUNT, followCount); // フォロー中の従業員データの件数
		putRequestScope(AttributeConst.PAGE, page); // ページ数
		putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); // 1ページに表示するレコードの数

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

		service.create(fv);
		redirect(ForwardConst.ACT_FOL, ForwardConst.CMD_INDEX);
	}
}
