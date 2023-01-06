package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.FollowConverter;
import actions.views.FollowView;
import constants.AttributeConst;
import constants.JpaConst;
import models.Employee;
import models.Follow;

//	フォローテーブルの操作に関わる処理を行うクラス
public class FollowService extends ServiceBase {

	/**
	 * idを条件に取得したデータをFollowViewのインスタンスで返却する
	 * @param id
	 * @return 取得データのインスタンス
	 */
	public FollowView findOne(int id) {
		return FollowConverter.toView(findOneInternal(id));
	}

	/**
	 * idを条件にデータを1件取得する
	 * @param id
	 * @return 取得データのインスタンス
	 */
	private Follow findOneInternal(int id) {
		return em.find(Follow.class, id);
	}

	//	指定したページ数の一覧画面に表示するデータを取得
	public List<FollowView> getFollows(Employee employee_id) {
		List<Follow> follows = em.createNamedQuery(JpaConst.Q_FOL_GET_FOLLOWS, Follow.class)
				.setParameter(JpaConst.JPQL_PARM_FOL_EMP, employee_id)
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
		System.out.println(fv.getFollow_id().getName() + " さんをフォローします");
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
		System.out.println("フォロー完了");
	}

	//	フォローデータを更新
	public void updateFollow(FollowView fv) {
		System.out.println("updateメソッド実行開始");
		//		更新日時に現在時刻を設定する
		LocalDateTime updateTime = LocalDateTime.now();
		fv.setUpdated_at(updateTime);
		//		トランザクション処理を開始
		em.getTransaction().begin();
		//		idを条件に従業員情報を取得
		Follow f = em.find(Follow.class, fv.getId());
		//		Viewモデルのフィールド内容をDTOモデルにコピー
		FollowConverter.copyViewToModel(f, fv);
		//		コミット
		em.getTransaction().commit();
		System.out.println("フォロー解除完了");
	}

	//	idを条件にフォロー中の従業員データを論理削除する
	//	id = レコードのid = 6 の時 従業員id = 16, 従業員名 = 國平琉希
	public void destroy(int id) {
		System.out.println("destroyメソッド実行開始");
		//		idを条件に登録済のフォローしている従業員情報を取得する
		//		idは従業員idではなくレコードのid（主キー）
		FollowView followedEmp = findOne(id);

		System.out.println("従業員名: " + followedEmp.getFollow_id().getName());

		//		取得した従業員の新設したisFollowを true → false に変更する
		followedEmp.getFollow_id().setIsFollow(false);

		//		取得した従業員のfollow_flagを 0 → 1 (削除済)にする
		followedEmp.setFollow_flag(AttributeConst.DEL_FLAG_TRUE.getIntegerValue());
		//		更新
		updateFollow(followedEmp);
	}
}
