package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = JpaConst.TABLE_FOL)
@NamedQueries({
		@NamedQuery(name = JpaConst.Q_FOL_GET_FOLLOWS, query = JpaConst.Q_FOL_GET_FOLLOWS_DEF),
		@NamedQuery(name = JpaConst.Q_FOL_COUNT, query = JpaConst.Q_FOL_COUNT_DEF),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Follow {

	//	id
	@Id
	@Column(name = JpaConst.FOL_COL_ID, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) // テーブルのidentity列を利用して主キー値を生成
	private Integer id;

	//	フォローする従業員の従業員テーブルのid
	@ManyToOne
	@JoinColumn(name = JpaConst.FOL_COL_EMP, nullable = false)
	private Employee employee_id;

	//	フォローされる従業員の従業員テーブルのid
	@ManyToOne
	@JoinColumn(name = JpaConst.FOL_COL_FOL, nullable = false)
	private Employee follow_id;

	//	登録日時
	@Column(name = JpaConst.FOL_COL_CREATED_AT, nullable = false)
	private LocalDateTime created_at;

	//	更新日時
	@Column(name = JpaConst.FOL_COL_UPDATED_AT, nullable = false)
	private LocalDateTime updated_at;

	//	フォロー論理削除フラグ
	@Column(name = JpaConst.FOL_COL_FOLLOW_FLAG, nullable = false)
	private Integer follow_flag;
}
