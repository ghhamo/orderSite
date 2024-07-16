package job.service.exception.global;

public class EntityAlreadyExists extends ValidationException {
    private final String name;

    public EntityAlreadyExists(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
