package com.matchskills.ia.service.exceptions.customs.jobposting;

public class JobPostingNotFoundException extends RuntimeException {
    public JobPostingNotFoundException() {
        super("JobPosting not found");
    }
}
