package services;

import javax.persistence.EntityManager;

import constants.JpaConst;
import utils.DBUtil;

// DB接続に関わる共通処理を行うクラス
public class ServiceBase {
	//	EntityManagerインスタンス
	protected EntityManager em = DBUtil.createEntityManager();

	//	EntityManagerのクローズ
	public void close() {
		if (em.isOpen()) {
			em.close();
		}
	}

	/**
	 * 従業員テーブルのデータの件数を取得し、返却する
	 * @return 従業員テーブルのデータの件数
	 */
	public long countAll() {
		long empCount = em.createNamedQuery(JpaConst.Q_EMP_COUNT, Long.class)
				.getSingleResult();

		return empCount;
	}
}
