package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import models.Employee;
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

		Employee employee_id = EmployeeConverter
				.toModel((EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP));

		List<EmployeeView> employees = employeeService.getPerPage(page);
		List<FollowView> follows = service.getFollows(employee_id);

		for (EmployeeView employee : employees) {
			for (FollowView follow : follows) {
				//				フォローボタンをクリックした従業員 かつ フォローフラグが 0 の時
				if (employee.getId() == follow.getFollow_id().getId()) {
					//		EmployeeViewに新設したフォローフラグをtrueにする
					System.out.println("フォローしている従業員: " + follow.getFollow_id().getName());
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

		redirect(ForwardConst.ACT_FOL, ForwardConst.CMD_INDEX);
	}

	public void destroy() throws ServletException, IOException {
		//		ログイン中の従業員のフォローリストを取得
		//		Employee employee_id = EmployeeConverter
		//				.toModel((EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP));
		//		List<FollowView> follower = service.getFollows(employee_id);
		List<FollowView> follower = getSessionScope(AttributeConst.FOLLOWS);

		//		フォローリストの中から フォローされる従業員id と送信されたリクエストパラメータ フォロー解除ボタンを押した解除対象の従業員id が同じ場合
		//		destroyメソッドを実行
		for (FollowView follow : follower) {
			System.out.println("フォロー中の従業員名: " + follow.getFollow_id().getName());
			if (follow.getFollow_id().getId() == toNumber(getRequestParam(AttributeConst.FOL_ID))) {
				System.out.println("フォローされる従業員のid: " + follow.getId());
				System.out.println("フォローされる従業員名: " + follow.getFollow_id().getName());
				//		論理削除メソッド
				service.destroy(follow.getId());
				break;
			} else {
				System.err.println(follow.getFollow_id().getName() + " さんのデータが見つかりませんでした。");
			}
		}

		//		一覧画面にリダイレクト
		redirect(ForwardConst.ACT_FOL, ForwardConst.CMD_INDEX);
	}

	public void followShow() throws ServletException, IOException {

	}
}
