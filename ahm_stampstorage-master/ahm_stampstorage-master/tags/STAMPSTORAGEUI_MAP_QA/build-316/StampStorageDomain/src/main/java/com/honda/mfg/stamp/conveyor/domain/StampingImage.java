package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;


@Entity
@Table(name = "IMAGE_TBX")
@Configurable
public class StampingImage {
	
	@Id
	private String imageName;

	@Lob
	@Column(name = "IMAGE_BYTES" ,length=4194304) 
	private byte[] imageBytes;




	@PersistenceContext
	    transient EntityManager entityManager;

	@Transactional
	    public void persist() {
	        if (this.entityManager == null) this.entityManager = entityManager();
	        this.entityManager.persist(this);
	    }

	@Transactional
	    public void remove() {
	        if (this.entityManager == null) this.entityManager = entityManager();
	        if (this.entityManager.contains(this)) {
	            this.entityManager.remove(this);
	        } else {
	            StampingImage attached = StampingImage.findStampingImage(this.imageName);
	            this.entityManager.remove(attached);
	        }
	    }

	@Transactional
	    public void flush() {
	        if (this.entityManager == null) this.entityManager = entityManager();
	        this.entityManager.flush();
	    }

	@Transactional
	    public void clear() {
	        if (this.entityManager == null) this.entityManager = entityManager();
	        this.entityManager.clear();
	    }

	@Transactional
	    public StampingImage merge() {
	        if (this.entityManager == null) this.entityManager = entityManager();
	        StampingImage merged = this.entityManager.merge(this);
	        this.entityManager.flush();
	        return merged;
	    }

	public static final EntityManager entityManager() {
	        EntityManager em = new StampingImage().entityManager;
	        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
	        return em;
	    }

	public static List<StampingImage> findAllStampingImages() {
	        return entityManager().createQuery("SELECT o FROM StampingImage o", StampingImage.class).getResultList();
	    }

	public static List<String> findImageNames(){
	    	  return entityManager().createQuery("SELECT o.imageName FROM StampingImage o", String.class).getResultList();
	    }

	public static StampingImage findStampingImage(String imageName) {
	        if (imageName == null) return null;
	        return entityManager().find(StampingImage.class, imageName);
	    }

	private String getImageName() {
		return this.imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public byte[] getImageBytes() {
		return this.imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("imageName: ").append(this.getImageName()).append(", ");
        sb.append("imageBytes: ").append((this.getImageBytes()!=null)?this.getImageBytes().length:"null");
       
        return sb.toString();
    }
}
