package dev.siliconcode.arc.datamodel;

import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * A representation of Script components such as those used in languages like Python,
 * Ruby, Groovy, and Java
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("scripts")
@BelongsTo(parent = Namespace.class, foreignKeyName = "namespace_id")
public class Script extends Component implements Measurable, ComponentContainer {

    @Override
    public List<Project> getParentProjects() {
        return null;
    }

    @Override
    public Reference createReference() {
        return null;
    }

    @Override
    public List<Type> getAllTypes() {
        return null;
    }

    @Override
    public List<Type> getClasses() {
        return null;
    }

    @Override
    public List<Type> getEnums() {
        return null;
    }

    @Override
    public List<Type> getInterfaces() {
        return null;
    }

    @Override
    public List<Member> getAllMembers() {
        return null;
    }

    @Override
    public List<Literal> getLiterals() {
        return null;
    }

    @Override
    public List<Initializer> getInitializers() {
        return null;
    }

    @Override
    public List<TypedMember> getAllTypedMembers() {
        return null;
    }

    @Override
    public List<Field> getFields() {
        return null;
    }

    @Override
    public List<Method> getAllMethods() {
        return null;
    }

    @Override
    public List<Method> getMethods() {
        return null;
    }

    @Override
    public List<Constructor> getConstructors() {
        return null;
    }

    @Override
    public List<Destructor> getDestructors() {
        return null;
    }

    @Override
    public String getRefKey() {
        return null;
    }

    @Override
    public Measurable getParent() {
        return null;
    }

    @Override
    public Project getParentProject() {
        return null;
    }

    @Override
    public File getParentFile() {
        return null;
    }
}
