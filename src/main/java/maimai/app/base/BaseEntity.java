package maimai.app.base;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity implements Serializable{
	private static final long serialVersionUID = 3546652661963711610L;
	public long id;
	public Date createTime;
	public Date updateTie;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTie() {
		return updateTie;
	}

	public void setUpdateTie(Date updateTie) {
		this.updateTie = updateTie;
	}
	
	
}
