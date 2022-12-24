package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.FollowConverter;
import actions.views.FollowView;
import constants.JpaConst;
import models.Follow;

//	フォローテーブルの操作に関わる処理を行うクラス
public class FollowService extends ServiceBase {
	//	idを条件に取得したデータをFollowViewのインスタンスを返却する
	public FollowView findOne(int id) {
		Follow f = findOneInternal(id);
		return FollowConverter.toView(f);
	}

	//	idを条件にデータを1件取得し、Followのインスタンスで返却する
	private Follow findOneInternal(int id) {
		Follow f = em.find(Follow.class, id);
		return f;
	}

	//	指定したページ数の一覧画面に表示するデータを取得
	public List<FollowView> getFollows(int page) {
		List<Follow> follows = em.createNamedQuery(JpaConst.Q_FOL_GET_FOLLOWS, Follow.class)
				.setFirstResult(JpaConst.ROW_PER_PAGE * page - 1)
				.setMaxResults(JpaConst.ROW_PER_PAGE)
				.getResultList();

		return FollowConverter.toViewList(follows);
	}

	//	フォロー中の従業員件数を取得
	public long getFollowCount() {
		long folCount = em.createNamedQuery(JpaConst.Q_FOL_COUNT, Long.class)
				.getSingleResult();

		return folCount;
	}

	//	フォローする従業員情報のデータを１件作成し、フォローテーブルに登録する
	public void create(FollowView fv) {
		//		現在日時を取得
		LocalDateTime ldt = LocalDateTime.now();
		//		登録日時にセット
		fv.setCreated_at(ldt);
		//		更新日時にセット
		fv.setUpdated_at(ldt);
		//		従業員情報をフォローテーブルに登録
		createFollow(fv);
	}

	//	フォローする従業員をフォローテーブルに登録
	public void createFollow(FollowView fv) {
		//		トランザクション処理を開始
		em.getTransaction().begin();
		//		従業員情報を保存
		em.persist(FollowConverter.toModel(fv));
		//		コミット
		em.getTransaction().commit();
	}

	//	フォローデータを更新
	public void updateFollow(FollowView fv) {
		//		トランザクション処理を開始
		em.getTransaction().begin();
		//		idを条件に従業員情報を取得
		Follow f = findOneInternal(fv.getId());
		//		Viewモデルのフィールド内容をDTOモデルにコピー
		FollowConverter.copyViewToModel(f, fv);
		//		コミット
		em.getTransaction().commit();
	}

	//	idを条件にフォロー中の従業員データを論理削除する
	public void destroyFollow(Integer id) {
		//		idを条件に登録済のフォローしている従業員情報を取得する
		FollowView followedEmp = findOne(id);

		//		更新日時に現在時刻を設定する
		LocalDateTime updateTime = LocalDateTime.now();
		followedEmp.setUpdated_at(updateTime);

		//		論理削除フラグをたてる
		followedEmp.setDelete_flag(JpaConst.FOL_DEL_TRUE);

		//		更新処理を行う
		updateFollow(followedEmp);
	}
}
