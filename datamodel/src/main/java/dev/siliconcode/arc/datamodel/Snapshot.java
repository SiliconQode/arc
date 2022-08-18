package dev.siliconcode.arc.datamodel;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;

import java.sql.Date;
import java.util.List;

/**
 * Represents either a full or partial snapshot of the system. Where a snapshot
 * represents a set of measurements for a project or part of a project at a given
 * date and time.
 *
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
@Many2Many(other = Project.class, sourceFKName = "snapshot_id", targetFKName = "project_id", join = "projects_snapshots")
@Many2Many(other = Type.class, sourceFKName = "snapshot_id", targetFKName = "type_id", join = "snapshots_unknowntypes")
public class Snapshot extends Model implements ComponentContainer {

    public Snapshot() {
    }

    /**
     * Constructs a new Snapshot of the given type for the current date and time
     *
     * @param type Type of the Snapshot to be created
     */
    public Snapshot(SnapshotType type) {
        set("type", type.value());
        setDate("datetime", new Date(java.lang.System.currentTimeMillis()));
    }

    @Override
    public List<Type> getAllTypes() {
        List<Type> types = Lists.newArrayList();
        getNamespaces().forEach(namespace -> types.addAll(namespace.getAllTypes()));
        return types;
    }

    public List<Namespace> getNamespaces() {
        return getAll(Namespace.class);
    }

    @Override
    public List<Type> getClasses() {
        List<Type> classes = Lists.newArrayList();
        getNamespaces().forEach(mod -> classes.addAll(mod.getClasses()));
        return classes;
    }

    @Override
    public List<Type> getEnums() {
        List<Type> enums = Lists.newArrayList();
        getNamespaces().forEach(mod -> enums.addAll(mod.getEnums()));
        return enums;
    }

    @Override
    public List<Type> getInterfaces() {
        List<Type> interfaces = Lists.newArrayList();
        getNamespaces().forEach(mod -> interfaces.addAll(mod.getInterfaces()));
        return interfaces;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = Lists.newArrayList();
        getNamespaces().forEach(mod -> members.addAll(mod.getAllMembers()));
        return members;
    }

    @Override
    public List<Literal> getLiterals() {
        List<Literal> literals = Lists.newArrayList();
        getNamespaces().forEach(mod -> literals.addAll(mod.getLiterals()));
        return literals;
    }

    @Override
    public List<Initializer> getInitializers() {
        List<Initializer> inits = Lists.newArrayList();
        getNamespaces().forEach(mod -> inits.addAll(mod.getInitializers()));
        return inits;
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        List<TypedMember> members = Lists.newArrayList();
        getNamespaces().forEach(mod -> members.addAll(mod.getAllTypedMembers()));
        return members;
    }

    @Override
    public List<Field> getFields() {
        List<Field> fields = Lists.newArrayList();
        getNamespaces().forEach(mod -> fields.addAll(mod.getFields()));
        return fields;
    }

    @Override
    public List<Method> getAllMethods() {
        List<Method> methods = Lists.newArrayList();
        getNamespaces().forEach(mod -> methods.addAll(mod.getAllMethods()));
        return methods;
    }

    @Override
    public List<Method> getMethods() {
        List<Method> methods = Lists.newArrayList();
        getNamespaces().forEach(mod -> methods.addAll(mod.getMethods()));
        return methods;
    }

    @Override
    public List<Constructor> getConstructors() {
        List<Constructor> constructors = Lists.newArrayList();
        getNamespaces().forEach(mod -> constructors.addAll(mod.getConstructors()));
        return constructors;
    }

    @Override
    public List<Destructor> getDestructors() {
        List<Destructor> destructors = Lists.newArrayList();
        getNamespaces().forEach(mod -> destructors.addAll(mod.getDestructors()));
        return destructors;
    }

    @Override
    public Project getParentProject() {
        return parent(Project.class);
    }

    /**
     * Gets the type of this snapshot
     *
     * @return The type of this snapshot
     */
    public SnapshotType getType() {
        if (get("type") == null)
            return null;
        else
            return SnapshotType.fromValue(getInteger("type"));
    }

    /**
     * Gets the date and time of this snapshot
     *
     * @return the java.sql.Date object representing the date and time of this snapshot
     */
    public Date getDateTime() {
        if (get("datetime") == null)
            return null;
        else
            return getDate("datetime");
    }

    /**
     * Gets the last full snapshot for a partial snapshot, or this snapshot if it is a full
     *
     * @return This snapshot if full, or the last full snapshot if this is a partial
     */
    public Snapshot getBaseSnapshot() {
        switch (getType()) {
            case FULL: return this;
            case PARTIAL: return getLastFull();
        }
        return null;
    }

    /**
     * Gets a list of all sibling partial snapshots, if this is a partial snapshot
     *
     * @return A list of sibling partial snapshots, if this is a partial snapshot, but if
     * this is a full snapshot, returns an empty list.
     */
    public List<Snapshot> getSiblingSnapshots() {
        switch (getType()) {
            case FULL: return Lists.newArrayList();
            case PARTIAL: return getOtherPartialsSinceLastFull();
        }
        return Lists.newArrayList();
    }

    private Snapshot getLastFull() {
        Project proj = getParentProject();
        return proj.getLastFull();
    }

    private List<Snapshot> getOtherPartialsSinceLastFull() {
        Snapshot full = getLastFull();
        Project proj = getParentProject();
        return proj.getPartialsSinceExceptID(full.getDateTime(), this.getLongId());
    }
}
