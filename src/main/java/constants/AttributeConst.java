package constants;

// 画面の項目値等を定義するEnumクラス
public enum AttributeConst {
	//フラッシュメッセージ
	FLUSH("flush"),

	//一覧画面共通
	MAX_ROW("maxRow"), PAGE("page"),

	//入力フォーム共通
	TOKEN("_token"), ERR("errors"),

	//ログイン中の従業員
	LOGIN_EMP("login_employee"),

	//ログイン画面
	LOGIN_ERR("loginError"),

	//従業員管理
	EMPLOYEE("employee"), EMPLOYEES("employees"), EMP_COUNT("employees_count"), EMP_ID("id"), EMP_CODE(
			"code"), EMP_PASS("password"), EMP_NAME("name"), EMP_ADMIN_FLG("admin_flag"),

	//管理者フラグ
	ROLE_ADMIN(1), ROLE_GENERAL(0),

	//削除フラグ
	DEL_FLAG_TRUE(1), DEL_FLAG_FALSE(0),

	//日報管理
	REPORT("report"), REPORTS("reports"), REP_COUNT("reports_count"), REP_ID("id"), REP_DATE("report_date"), REP_TITLE(
			"title"), REP_CONTENT("content_msg"), REP_FOL("follow_reports"), REP_FOL_COUNT(
					"follow_reports_count"), REP_FOLLOWER(
							"follower_reports"), REP_FOLLOWER_COUNT("follower_reports_count"),

	//	フォロー管理
	FOLLOW("follow"), FOLLOWS("follows"), FOL_COUNT("follows_count"), FOL_ID("id"), FOL_EMP_ID(
			"employee_id"), FOL_FOL_ID("follow_id"), FOLLOWER("follower"), FOLLOWER_COUNT("follower_count");

	private final String text;
	private final Integer i;

	private AttributeConst(final String text) {
		this.text = text;
		this.i = null;
	}

	private AttributeConst(final Integer i) {
		this.text = null;
		this.i = i;
	}

	public String getValue() {
		return this.text;
	}

	public Integer getIntegerValue() {
		return this.i;
	}

}