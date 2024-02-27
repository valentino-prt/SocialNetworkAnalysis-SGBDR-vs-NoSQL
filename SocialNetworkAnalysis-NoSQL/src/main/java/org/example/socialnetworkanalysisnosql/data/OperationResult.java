package org.example.socialnetworkanalysisnosql.data;

public class OperationResult {
    private final String message;
    private final long executionTime; // Temps en millisecondes

    public OperationResult(String message, long executionTime) {
        this.message = message;
        this.executionTime = executionTime;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}
