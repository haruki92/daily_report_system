package actions.views;

import java.util.ArrayList;
import java.util.List;

import constants.AttributeConst;
import constants.JpaConst;
import models.Follow;

public class FollowConverter {
	//	ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
	public static Follow toModel(FollowView fv) {
		return new Follow(
				fv.getId(),
				EmployeeConverter.toModel(fv.getEmployee_id()),
				EmployeeConverter.toModel(fv.getFollow_id()),
				fv.getCreated_at(),
				fv.getUpdated_at(),
				fv.getFollow_flag() == null
						? null
						: fv.getFollow_flag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
								? JpaConst.FOL_DEL_TRUE
								: JpaConst.FOL_DEL_FALSE);
	}

	//	DTOモデルのインスタンスからViewモデルのインスタンスを作成する
	public static FollowView toView(Follow f) {
		if (f == null) {
			return null;
		}

		return new FollowView(
				f.getId(),
				EmployeeConverter.toView(f.getEmployee_id()),
				EmployeeConverter.toView(f.getFollow_id()),
				f.getCreated_at(),
				f.getUpdated_at(),
				f.getFollow_flag() == null
						? null
						: f.getFollow_flag() == JpaConst.FOL_DEL_TRUE
								? AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
								: AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
	}

	//	DTOモデルのリストからViewモデルのリストを作成する
	public static List<FollowView> toViewList(List<Follow> list) {
		List<FollowView> fvs = new ArrayList<>();

		for (Follow f : list) {
			fvs.add(toView(f));
		}

		return fvs;
	}

	//	Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
	public static void copyViewToModel(Follow f, FollowView fv) {
		f.setId(fv.getId());
		f.setEmployee_id(EmployeeConverter.toModel(fv.getEmployee_id()));
		f.setFollow_id(EmployeeConverter.toModel(fv.getFollow_id()));
		f.setCreated_at(fv.getCreated_at());
		f.setUpdated_at(fv.getUpdated_at());
		f.setFollow_flag(fv.getFollow_flag());
	}

}
