package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//フォロー情報についての画面の入力値・出力値を扱うViewモデル
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowView {
	//	id
	private Integer id;

	//	フォローする従業員の従業員テーブルのid
	private EmployeeView employee_id;

	//	フォローされる従業員の従業員テーブルのid
	private EmployeeView follow_id;

	//	登録日時
	private LocalDateTime created_at;

	//	更新日時
	private LocalDateTime updated_at;

	//	削除フラグ
	private Integer delete_flag;
}
