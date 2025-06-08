package com.honda.galc.client.teamlead.checker.compare;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dto.AbstractCheckerDto;
/**
 * 
 * <h3>AbstractMapperConverter Class description</h3>
 * <p> AbstractMapperConverter: Abstract Class for Checker Dto Mapper Converters</p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 *   
 * @author Hemant Kumar<br>
 * Apr 30, 2018
 *
 */
public abstract class AbstractMapperConverter<E extends AbstractCheckerDto> {

	private List<E> fromDataList;
	private List<E> toDataList;

	public AbstractMapperConverter(List<E> fromDataList, List<E> toDataList) {
		super();
		this.fromDataList = fromDataList;
		this.toDataList = toDataList;
	}

	public abstract List<? extends AbstractDtoMapper<E>> getMapperList();

	/**
	 * This method is used to get Checker Data by Key
	 * @param key
	 * @param isFromData
	 * @return
	 */
	public List<E> getCheckerDataByKey(String key, boolean isFromData) {
		List<E> list = new ArrayList<E>();
		if(isFromData) {
			for(E dto : fromDataList) {
				if(dto.getKey().equals(key)) {
					list.add(dto);
				}
			}
		} else {
			for(E dto : toDataList) {
				if(dto.getKey().equals(key)) {
					list.add(dto);
				}
			}
		}
		return list;
	}

	/**
	 * This method is used to check if there is any difference or not
	 * @param mappedData
	 * @param clazz
	 * @return
	 */
	protected <M extends AbstractDtoMapper<E>> boolean isMapperDataValid(M mapper, Class<M> clazz) {
		if(mapper.getFromCheckers().size() != mapper.getToCheckers().size()) {
			return true;
		}
		for(E fromChecker : mapper.getFromCheckers()) {	
			for(E toChecker : mapper.getToCheckers()) {
				if(toChecker.getCheckSeq() == fromChecker.getCheckSeq() || toChecker.getCheckSeq() == 0) {
					if(!(toChecker.getCheckPoint().equals(fromChecker.getCheckPoint())
							&& toChecker.getCheckName().equals(fromChecker.getCheckName())
							&& toChecker.getChecker().equals(fromChecker.getChecker())
							&& toChecker.getReactionType().equals(fromChecker.getReactionType()))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected List<E> getFromDataList() {
		return fromDataList;
	}

	protected List<E> getToDataList() {
		return toDataList;
	}
}
