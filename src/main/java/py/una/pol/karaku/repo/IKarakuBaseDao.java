package py.una.pol.karaku.repo;

import java.io.Serializable;
import py.una.pol.karaku.dao.BaseDAO;

public interface IKarakuBaseDao<T, K extends Serializable> extends
		BaseDAO<T, K> {

}
