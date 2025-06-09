package com.honda.mfg.stamp.conveyor.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "MODEL_TBX")
@Configurable
public class Model {

	@NotNull
	@Column(name = "NAME")
	private String name;

	@NotNull
	@Column(name = "DESCRIPTION")
	private String description;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="LEFT_DIE", referencedColumnName="DIE_ID")
	private Die leftDie;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="RIGHT_DIE", referencedColumnName="DIE_ID")
	private Die rightDie;

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MODEL_ID")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Model attached = Model.findModel(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public Model merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Model merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new Model().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countModels() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Model o", Long.class).getSingleResult();
	}

	public static List<Model> findAllModels() {
		return entityManager().createQuery("SELECT o FROM Model o  order by o.name", Model.class).getResultList();
	}

	public static List<Model> findActiveModels() {
		Collection<Model> models = Model.findAllModels();
		List<Model> activeModels = new ArrayList<Model>();

		for (Model model : models) {
			if (model.getActive()) {
				activeModels.add(model);
			}
		}

		return activeModels;
	}

	public static Model findModel(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Model.class, id);
	}

	public static List<Model> findModelEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Model o", Model.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Die getLeftDie() {
		return this.leftDie;
	}

	public void setLeftDie(Die leftDie) {
		this.leftDie = leftDie;
	}

	public Die getRightDie() {
		return this.rightDie;
	}

	public void setRightDie(Die rightDie) {
		this.rightDie = rightDie;
	}

	public boolean getActive() {
		if ((this.rightDie != null && !this.rightDie.getActive())
				|| (this.leftDie != null && !this.leftDie.getActive())) {
			return false;
		}

		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
//        sb.append("Model.Id: ").append(getId()).append(", ");
//        sb.append("Model.Description: ").append(getDescription()).append(", ");
//        sb.append("Model.Name: ").append(getName()).append(", ");
//        sb.append("Model.LeftDie: ").append(getLeftDie()).append(", ");
//        sb.append("Model.RightDie: ").append(getRightDie()).append(", ");
//        sb.append("Model.Version: ").append(getVersion());
		sb.append(getName());
		return sb.toString();
	}
}
