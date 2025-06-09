package com.honda.shared.domain;

/**
 * User: Jeffrey M Lutz Date: 2/14/11
 */
public class EntityBase implements Entity<EntityBase> {
	private long id;

	public EntityBase(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj.getClass().equals(this.getClass()))) {
			return false;
		}
		EntityBase lhs = this;
		EntityBase rhs = (EntityBase) obj;

		return lhs.hashCode() == rhs.hashCode();
	}

	@Override
	public int hashCode() {
		return new Long(id).hashCode();
	}

	public boolean sameIdentityAs(EntityBase that) {
		return this.getId() == that.getId();
	}
}
