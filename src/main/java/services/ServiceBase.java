package services;

import java.util.List;

import javax.persistence.EntityManager;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import constants.JpaConst;
import models.Employee;
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
	 * 指定されたページ数の一覧画面に表示するデータを取得し、EmployeeViewのリストで返却する
	 * @param page ページ数
	 * @return 表示するデータのリスト
	 */
	public List<EmployeeView> getPerPage(int page) {
		List<Employee> employees = em.createNamedQuery(JpaConst.Q_EMP_GET_ALL, Employee.class)
				.setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
				.setMaxResults(JpaConst.ROW_PER_PAGE)
				.getResultList();

		return EmployeeConverter.toViewList(employees);
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
