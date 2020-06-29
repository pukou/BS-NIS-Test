package com.bsoft.nis.service.catheter.support;


import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.catheter.CatheterMeasurData;
import com.bsoft.nis.domain.catheter.CatheterSpinnerData;
import com.bsoft.nis.domain.catheter.db.CatheterYLGJLvo;
import com.bsoft.nis.domain.lifesign.LifeSignRealSaveDataItem;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.mapper.catheter.CatheterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 *
 */
@Service
public class CatheterService extends RouteDataSourceService {

	@Autowired
	CatheterMapper mapper;

	//    /**
	//     * 获取引流管医嘱列表
	//     * @param kssj  开始时间
	//     * @param jssj  结束时间
	//     * @param brbq  病人病区
	//     * @return
	//     */
	//    public List<Advice> getAdvice(String kssj,String jssj,String brbq)
	//            throws SQLException,DataAccessException {
	//        return  mapper.getAdvice(kssj,jssj,brbq);
	//    }


	/**
	 * 获取病人列表
	 *
	 * @param kssj
	 * @param jssj
	 * @param brbq
	 * @param jgid
	 * @param xmlxs
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<SickPersonVo> getpationtList(String kssj, String jssj, String brbq,
			String jgid, List<String> xmxhs, List<String> xmlxs) throws Exception {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getpationtList(kssj, jssj, brbq, jgid, xmxhs, xmlxs, dbtype);
	}

	/**
	 * 获取需测数据
	 *
	 * @param brbq
	 * @param jgid
	 * @param zyh
	 * @param kssj
	 * @param jssj
	 * @param xmlxs
	 * @return
	 */
	public List<CatheterMeasurData> getMeasurData(String brbq, String jgid, String zyh,
			String kssj, String jssj, List<String> xmxhs, List<String> xmlxs)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getMeasurData(brbq, jgid, zyh, kssj, jssj, xmxhs, xmlxs, dbtype);
	}

	/**
	 * 获取已测数据
	 *
	 * @param brbq
	 * @param jgid
	 * @param zyh
	 * @param jlrq
	 * @return
	 */
	public List<CatheterYLGJLvo> getYLGJL(String brbq, String jgid, String zyh, String jlrq)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getYLGJL(brbq, jgid, zyh, jlrq, dbtype);
	}

	public List<String> getDZXM() throws SQLException, DataAccessException {
		return mapper.getDZXM();
	}

	@Transactional(rollbackFor = Exception.class)
	public Integer saveCatheter(List<CatheterYLGJLvo> list)
			throws SQLException, DataAccessException {
		Integer num = 0;
		String dbtype = getCurrentDataSourceDBtype();
		for (CatheterYLGJLvo catheterYLGJLvo : list) {
			catheterYLGJLvo.dbtype = dbtype;
			num += mapper.saveCatheter(catheterYLGJLvo);
		}
		return num;
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean cancelCatheter(String jlxh, String jgid)
			throws SQLException, DataAccessException {
		return mapper.cancelCatheter(jlxh, jgid);
	}

	public List<CatheterSpinnerData> getSpinnerData(String ypxh)
			throws SQLException, DataAccessException {
		return mapper.getSpinnerData(ypxh);
	}

	@Transactional(rollbackFor = Exception.class)
	public Integer saveLifeSign(List<LifeSignRealSaveDataItem> list)
			throws SQLException, DataAccessException {
		Integer num = 0;
		String dbtype = getCurrentDataSourceDBtype();
		for (LifeSignRealSaveDataItem item : list) {
			num += mapper.saveLifeSign(item, dbtype);
		}
		return num;
	}

	public String getLifeSignTwdxsByXmh(String tzxm)
			throws SQLException, DataAccessException {
		return mapper.getLifeSignTwdxsByXmh(tzxm);
	}

	public Integer deleteSmtzFromCatheter(String cjh, String jgid)
			throws SQLException, DataAccessException {
		return mapper.deleteSmtzFromCatheter(cjh, jgid);

	}

	public String getTzcjhByJlxh(String jlxh, String jgid)
			throws SQLException, DataAccessException {
		return mapper.getTzcjhByJlxh(jlxh, jgid);
	}

	public List<String> getXmlxByXmxh(List<String> xmxhs)
			throws SQLException, DataAccessException {
		return mapper.getXmlxByXmxh(xmxhs);
	}
}
