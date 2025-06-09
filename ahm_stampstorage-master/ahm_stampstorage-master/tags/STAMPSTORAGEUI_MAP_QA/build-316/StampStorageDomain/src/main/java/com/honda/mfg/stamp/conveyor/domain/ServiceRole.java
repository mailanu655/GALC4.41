package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Max;

@Entity
@Table(name = "SERVICE_ROLE_TBX")
@Configurable
/**Defines the Role of a node and failovers*/
public class ServiceRole {

    @NotNull
    @Column(name = "PORT")
    @Max(65535L)
    private int port;

    @NotNull
    @Column(name = "IP_ADDR")
    private String ip;

    @NotNull
    @Column(name = "SERVICE_NAME")
    private String serviceName;

    @NotNull
    @Column(name = "DESIGNATED_PRI")
    private Boolean designatedPrimary;

    @Column(name = "FAILOVER_ORDER")
    private int failoverOrder;

    @NotNull
    @Column(name = "CURRENT_ACTIVE")
    private Boolean currentActive;

    @Column(name = "HOSTNAME")
    private String hostName;
        

	public static TypedQuery<ServiceRole> findServiceRolesByCurrentActiveNot(Boolean currentActive) {
        if (currentActive == null) throw new IllegalArgumentException("The currentActive argument is required");
        EntityManager em = ServiceRole.entityManager();
        TypedQuery<ServiceRole> q = em.createQuery("SELECT o FROM ServiceRole AS o WHERE o.currentActive IS NOT :currentActive", ServiceRole.class);
        q.setParameter("currentActive", currentActive);
        return q;
    }

	public static TypedQuery<ServiceRole> findServiceRolesByHostNameEquals(String hostName) {
        if (hostName == null || hostName.length() == 0) throw new IllegalArgumentException("The hostName argument is required");
        EntityManager em = ServiceRole.entityManager();
        TypedQuery<ServiceRole> q = em.createQuery("SELECT o FROM ServiceRole AS o WHERE o.hostName = :hostName", ServiceRole.class);
        q.setParameter("hostName", hostName);
        return q;
    }

	public static TypedQuery<ServiceRole> findServiceRolesByIpEqualsAndPort(String ip, int port) {
        if (ip == null || ip.length() == 0) throw new IllegalArgumentException("The ip argument is required");
        EntityManager em = ServiceRole.entityManager();
        TypedQuery<ServiceRole> q = em.createQuery("SELECT o FROM ServiceRole AS o WHERE o.ip = :ip  AND o.port = :port", ServiceRole.class);
        q.setParameter("ip", ip);
        q.setParameter("port", port);
        return q;
    }

	public static TypedQuery<ServiceRole> findServiceRolesByIpEqualsAndPortAndCurrentActiveNot(String ip, int port, Boolean currentActive) {
        if (ip == null || ip.length() == 0) throw new IllegalArgumentException("The ip argument is required");
        if (currentActive == null) throw new IllegalArgumentException("The currentActive argument is required");
        EntityManager em = ServiceRole.entityManager();
        TypedQuery<ServiceRole> q = em.createQuery("SELECT o FROM ServiceRole AS o WHERE o.ip = :ip  AND o.port = :port AND o.currentActive IS NOT :currentActive", ServiceRole.class);
        q.setParameter("ip", ip);
        q.setParameter("port", port);
        q.setParameter("currentActive", currentActive);
        return q;
    }

	public static TypedQuery<ServiceRole> findServiceRolesByIpEqualsAndPortAndDesignatedPrimaryNot(String ip, int port, Boolean designatedPrimary) {
        if (ip == null || ip.length() == 0) throw new IllegalArgumentException("The ip argument is required");
        if (designatedPrimary == null) throw new IllegalArgumentException("The designatedPrimary argument is required");
        EntityManager em = ServiceRole.entityManager();
        TypedQuery<ServiceRole> q = em.createQuery("SELECT o FROM ServiceRole AS o WHERE o.ip = :ip  AND o.port = :port AND o.designatedPrimary IS NOT :designatedPrimary", ServiceRole.class);
        q.setParameter("ip", ip);
        q.setParameter("port", port);
        q.setParameter("designatedPrimary", designatedPrimary);
        return q;
    }

	public static TypedQuery<ServiceRole> findServiceRolesByServiceNameEquals(String serviceName) {
        if (serviceName == null || serviceName.length() == 0) throw new IllegalArgumentException("The serviceName argument is required");
        EntityManager em = ServiceRole.entityManager();
        TypedQuery<ServiceRole> q = em.createQuery("SELECT o FROM ServiceRole AS o WHERE o.serviceName = :serviceName", ServiceRole.class);
        q.setParameter("serviceName", serviceName);
        return q;
    }

	public static TypedQuery<ServiceRole> findServiceRolesByServiceNameEqualsAndCurrentActiveNot(String serviceName, Boolean currentActive) {
        if (serviceName == null || serviceName.length() == 0) throw new IllegalArgumentException("The serviceName argument is required");
        if (currentActive == null) throw new IllegalArgumentException("The currentActive argument is required");
        EntityManager em = ServiceRole.entityManager();
        TypedQuery<ServiceRole> q = em.createQuery("SELECT o FROM ServiceRole AS o WHERE o.serviceName = :serviceName  AND o.currentActive IS NOT :currentActive", ServiceRole.class);
        q.setParameter("serviceName", serviceName);
        q.setParameter("currentActive", currentActive);
        return q;
    }

	public static TypedQuery<ServiceRole> findServiceRolesByServiceNameEqualsAndDesignatedPrimaryNot(String serviceName, Boolean designatedPrimary) {
        if (serviceName == null || serviceName.length() == 0) throw new IllegalArgumentException("The serviceName argument is required");
        if (designatedPrimary == null) throw new IllegalArgumentException("The designatedPrimary argument is required");
        EntityManager em = ServiceRole.entityManager();
        TypedQuery<ServiceRole> q = em.createQuery("SELECT o FROM ServiceRole AS o WHERE o.serviceName = :serviceName  AND o.designatedPrimary IS NOT :designatedPrimary", ServiceRole.class);
        q.setParameter("serviceName", serviceName);
        q.setParameter("designatedPrimary", designatedPrimary);
        return q;
    }

	public static TypedQuery<ServiceRole> findServiceRolesByServiceNameEqualsAndIpEqualsAndPort(String serviceName, String ip, int port) {
        if (serviceName == null || serviceName.length() == 0) throw new IllegalArgumentException("The serviceName argument is required");
        if (ip == null || ip.length() == 0) throw new IllegalArgumentException("The ip argument is required");
        EntityManager em = ServiceRole.entityManager();
        TypedQuery<ServiceRole> q = em.createQuery("SELECT o FROM ServiceRole AS o WHERE o.serviceName = :serviceName  AND o.ip = :ip  AND o.port = :port", ServiceRole.class);
        q.setParameter("serviceName", serviceName);
        q.setParameter("ip", ip);
        q.setParameter("port", port);
        return q;
    }

	public static List<ServiceRole> findAllServiceRolesOrderByFailoverOrder() {
        return entityManager().createQuery("SELECT o FROM ServiceRole o where failover_order > 0 order by failover_order", ServiceRole.class).getResultList();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SERVICE_ID")
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
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ServiceRole attached = ServiceRole.findServiceRole(this.id);
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
    public ServiceRole merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ServiceRole merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new ServiceRole().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countServiceRoles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ServiceRole o", Long.class).getSingleResult();
    }

	public static List<ServiceRole> findAllServiceRoles() {
        return entityManager().createQuery("SELECT o FROM ServiceRole o", ServiceRole.class).getResultList();
    }

	public static ServiceRole findServiceRole(Long id) {
        if (id == null) return null;
        return entityManager().find(ServiceRole.class, id);
    }

	public static List<ServiceRole> findServiceRoleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ServiceRole o", ServiceRole.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public int getPort() {
        return this.port;
    }

	public void setPort(int port) {
        this.port = port;
    }

	public String getIp() {
        return this.ip;
    }

	public void setIp(String ip) {
        this.ip = ip;
    }

	public String getServiceName() {
        return this.serviceName;
    }

	public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

	public Boolean getDesignatedPrimary() {
        return this.designatedPrimary;
    }

	public void setDesignatedPrimary(Boolean designatedPrimary) {
        this.designatedPrimary = designatedPrimary;
    }

	public int getFailoverOrder() {
        return this.failoverOrder;
    }

	public void setFailoverOrder(int failoverOrder) {
        this.failoverOrder = failoverOrder;
    }

	public Boolean getCurrentActive() {
        return this.currentActive;
    }

	public void setCurrentActive(Boolean currentActive) {
        this.currentActive = currentActive;
    }

	public String getHostName() {
        return this.hostName;
    }

	public void setHostName(String hostName) {
        this.hostName = hostName;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CurrentActive: ").append(getCurrentActive()).append(", ");
        sb.append("DesignatedPrimary: ").append(getDesignatedPrimary()).append(", ");
        sb.append("FailoverOrder: ").append(getFailoverOrder()).append(", ");
        sb.append("HostName: ").append(getHostName()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Ip: ").append(getIp()).append(", ");
        sb.append("Port: ").append(getPort()).append(", ");
        sb.append("ServiceName: ").append(getServiceName()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
