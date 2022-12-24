package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.FollowService;

public class FollowAction extends ActionBase {
	private FollowService service;

	@Override
	public void process() throws ServletException, IOException {
		service = new FollowService();

		//		メソッドを実行
		invoke();
		service.close();
	}

	//	一覧画面を表示する
	public void index() throws ServletException, IOException {
		System.out.println("Action:" + ForwardConst.ACT_FOL + "command:" + ForwardConst.CMD_INDEX + "を実行します");
		//		指定されたページ数の一覧画面に表示するデータを取得
		int page = getPage();
		List<FollowView> follows = service.getFollows(page);

		//		全ての従業員データの件数を取得
		long followCount = service.getFollowCount();

		putRequestScope(AttributeConst.FOLLOWS, follows); // 取得したフォロー中の従業員データ
		putRequestScope(AttributeConst.FOL_COUNT, followCount); // フォロー中の従業員データの件数
		putRequestScope(AttributeConst.PAGE, page); // ページ数
		putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); // 1ページに表示するレコードの数

		//		一覧画面を表示
		forward(ForwardConst.FW_FOL_INDEX);
	}
}
