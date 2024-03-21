package task.technical.mathematicalassistant.models.logic;

import lombok.Getter;

public class RootValidationModel {
    @Getter
    private final String root;

    public RootValidationModel(String root) {
        this.root = root.trim().toLowerCase();
    }


    public RootValidationModel validateRootElement () throws IllegalArgumentException {
        String regexRootInCorrectFormat = "-?\\d+(\\.\\d+)?";
        if(!root.matches(regexRootInCorrectFormat)) {
            throw new IllegalArgumentException("Incorrect format of the root value");
        }
        return this;
    }

}
