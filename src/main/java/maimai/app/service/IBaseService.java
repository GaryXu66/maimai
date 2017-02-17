package maimai.app.service;

public interface IBaseService {
	
	public <T> T getEntityById(long id, Class t);

}
