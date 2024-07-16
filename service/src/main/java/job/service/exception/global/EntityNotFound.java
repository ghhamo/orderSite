package job.service.exception.global;

public class EntityNotFound extends ValidationException {
    @java.io.Serial
    private static final long serialVersionUID = 230129027920973647L;

    private final String name;

    public EntityNotFound(String name) {
        this.name = name;
    }

    public String getUuid() {
        return name;
    }
}
