package fr.univ_lyon1.info.m1.microblog.dto;

/**
 * Data Transfer Object for the User class.
 */
public class UserDTO {
    private String usertag;
    private String userStrategy;

    /**
     * Gets the usertag.
     *
     * @return usertag
     */
    public String getUsertag() {
        return usertag;
    }

    /**
     * Sets the usertag.
     *
     * @param usertag usertag
     */
    public void setUsertag(final String usertag) {
        this.usertag = usertag;
    }

    /**
     * Gets the current strategy of the user.
     *
     * @return userStrategy
     */
    public String getUserStrategy() {
        return userStrategy;
    }

    /**
     * Sets the current strategy of the user.
     *
     * @param userStrategy usertag
     */
    public void setUserStrategy(final String userStrategy) {
        this.userStrategy = userStrategy;
    }
}
