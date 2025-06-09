package com.honda.mfg.stamp.conveyor.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotNull;
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
import com.honda.mfg.stamp.conveyor.domain.enums.SEVERITY;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Configurable
@Entity
@Table(name = "AUDIT_ERROR_LOG_TBX")
public class AuditErrorLog {
	  private static final Logger LOG = LoggerFactory.getLogger(AuditErrorLog.class);

    @NotNull
    @Column(name = "NODE_ID")
    private String nodeId;

    @NotNull
    @Column(name = "SOURCE")
    private String source;

    @NotNull
    @Column(name = "SEVERITY")
    private Integer severity;

    @Column(name = "MESSAGE_TEXT")
    private String messageText;

    @Column(name = "AUDIT_ERROR_LOG_TSTP")
    private Timestamp logTimestamp;

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("logTimestamp: ").append(getLogTimestamp()).append(", ");
        sb.append("messageText: ").append(getMessageText()).append(", ");
        sb.append("nodeId: ").append(getNodeId()).append(", ");
        sb.append("severity: ").append(getSeverity()).append(", ");
        sb.append("source: ").append(getSource()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public static List<AuditErrorLog> findAuditErrorLogByCriteria(AuditErrorLogFinderCriteria finderCriteria, Integer page, Integer sizeNo) {
        EntityManager em = AuditErrorLog.entityManager();
        TypedQuery<AuditErrorLog> q;
        String sql = "SELECT o FROM AuditErrorLog AS o ";
        List<String> criteriaList = new ArrayList<String>();
        q = em.createQuery(sql, AuditErrorLog.class);

     //   LOG.debug("Finder: nodeId: " + finderCriteria.getNodeId());
        if (finderCriteria.getNodeId() != null) {
            criteriaList.add("o.nodeId like :nodeId");
        }
        if (finderCriteria.getSource() != null) {
            criteriaList.add("o.source like :source");
        }
        if (finderCriteria.getSeverity() != null) {
            criteriaList.add("o.severity = :severity");
        }
        if (finderCriteria.getMessageText() != null) {
     //       LOG.debug("messageText: " + finderCriteria.getMessageText());
            criteriaList.add("o.messageText like :messageText");
        }
        if ((finderCriteria.getBeginTimestamp() != null) || (finderCriteria.getEndTimestamp() != null)) {
            criteriaList.add("o.logTimestamp between :beginTimestamp and :endTimestamp");
        }

        if (criteriaList.size() > 0) {
            sql = sql + " where ";

            String tempsql = "";
            tempsql = criteriaList.get(0);
            for (int i = 1; i < criteriaList.size(); i++) {
                tempsql = tempsql + " AND " + criteriaList.get(i);
            }
            String orderBySql = " order by o.id desc";
            q = em.createQuery(sql + tempsql + orderBySql, AuditErrorLog.class);

            if (finderCriteria.getNodeId() != null) {
                q.setParameter("nodeId", "%" + finderCriteria.getNodeId() + "%");
            }
            if (finderCriteria.getSource() != null) {
                q.setParameter("source", "%" + finderCriteria.getSource() + "%");
            }
            if (finderCriteria.getSeverity() != null) {
                q.setParameter("severity", finderCriteria.getSeverity());
            }
            if (finderCriteria.getMessageText() != null) {
                q.setParameter("messageText", "%" + finderCriteria.getMessageText() + "%");
            }
            if ((finderCriteria.getBeginTimestamp() != null) && (finderCriteria.getEndTimestamp() != null)) {
                q.setParameter("beginTimestamp", finderCriteria.getBeginTimestamp());
                q.setParameter("endTimestamp", finderCriteria.getEndTimestamp());
            }
        }
        if (page == null || sizeNo == null) {
            return q.getResultList();
        } else {
            return q.setFirstResult(page == null ? 0 : (page.intValue() - 1) * sizeNo).setMaxResults(sizeNo).getResultList();
        }
    }

	public static Long findAuditErrorLogCount(AuditErrorLogFinderCriteria finderCriteria) {

        EntityManager em = AuditErrorLog.entityManager();
        TypedQuery<Long> q = null;
        String sql = "SELECT count(o) FROM AuditErrorLog AS o ";
        List<String> criteriaList = new ArrayList<String>();

        if (finderCriteria.getNodeId() != null) {
            criteriaList.add("o.nodeId like :nodeId");
        }
        if (finderCriteria.getSource() != null) {
            criteriaList.add("o.source like :source");
        }
        if (finderCriteria.getSeverity() != null) {
            criteriaList.add("o.severity = :severity");
        }
        if (finderCriteria.getMessageText() != null) {
            criteriaList.add("o.messageText like :messageText");
        }
        if ((finderCriteria.getBeginTimestamp() != null) && (finderCriteria.getEndTimestamp() != null)) {
            criteriaList.add("o.logTimestamp between :beginTimestamp and :endTimestamp");
        }

        if (criteriaList.size() > 0) {
            sql = sql + " where ";

            String tempsql = "";
            tempsql = criteriaList.get(0);
            for (int i = 1; i < criteriaList.size(); i++) {
                tempsql = tempsql + " AND " + criteriaList.get(i);
            }
            q = em.createQuery(sql + tempsql, Long.class);

            if (finderCriteria.getNodeId() != null) {
                q.setParameter("nodeId", "%" + finderCriteria.getNodeId() + "%");
            }
            if (finderCriteria.getSource() != null) {
                q.setParameter("source", "%" + finderCriteria.getSource() + "%");
            }
            if (finderCriteria.getSeverity() != null) {
                q.setParameter("severity", finderCriteria.getSeverity());
            }
            if (finderCriteria.getMessageText() != null) {
                q.setParameter("messageText", "%" + finderCriteria.getMessageText() + "%");
            }
            if ((finderCriteria.getBeginTimestamp() != null) || (finderCriteria.getEndTimestamp() != null)) {
                q.setParameter("beginTimestamp", finderCriteria.getBeginTimestamp());
                q.setParameter("endTimestamp", finderCriteria.getEndTimestamp());
            }
        }
        if (q == null) {
            q = em.createQuery(sql, Long.class);
        }
        Long count = q.getSingleResult();
        return count != null ? count : 0;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LOG_ID")
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
            AuditErrorLog attached = AuditErrorLog.findAuditErrorLog(this.id);
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
    public AuditErrorLog merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AuditErrorLog merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new AuditErrorLog().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAuditErrorLogs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AuditErrorLog o", Long.class).getSingleResult();
    }

	public static List<AuditErrorLog> findAllAuditErrorLogs() {
        return entityManager().createQuery("SELECT o FROM AuditErrorLog o", AuditErrorLog.class).getResultList();
    }

	public static AuditErrorLog findAuditErrorLog(Long id) {
        if (id == null) return null;
        return entityManager().find(AuditErrorLog.class, id);
    }

	public static List<AuditErrorLog> findAuditErrorLogEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AuditErrorLog o", AuditErrorLog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public static void save(String nodeId, String message, String source) {
                AuditErrorLog auditErrorLogEntry = new AuditErrorLog();
                auditErrorLogEntry.setLogTimestamp(new Timestamp(System.currentTimeMillis()));
                auditErrorLogEntry.setNodeId(nodeId);
                auditErrorLogEntry.setMessageText(message);
                auditErrorLogEntry.setSeverity(SEVERITY.FOUR.type());
                auditErrorLogEntry.setSource(source);

                auditErrorLogEntry.persist();
    }

	public String getNodeId() {
        return this.nodeId;
    }

	public void setNodeId(String NodeId) {
        this.nodeId = NodeId;
    }

	public String getSource() {
        return this.source;
    }

	public void setSource(String Source) {
        this.source = Source;
    }

	public Integer getSeverity() {
        return this.severity;
    }

	public void setSeverity(Integer Severity) {
        this.severity = Severity;
    }

	public String getMessageText() {
        return this.messageText;
    }

	public void setMessageText(String MessageText) {
        this.messageText = MessageText;
    }

	public Timestamp getLogTimestamp() {
        return this.logTimestamp;
    }

	public void setLogTimestamp(Timestamp LogTimestamp) {
        this.logTimestamp = LogTimestamp;
    }
}
