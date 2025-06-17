package fr.univ_lyon1.info.m1.microblog.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the UserDTO class.
 */
public class UserDTOTest {

    private UserDTO userDTO;

    /**
     * Setup method to initialize a UserDTO object before each test.
     */
    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
    }

    /**
     * Test the default value of usertag.
     */
    @Test
    public void testDefaultUsertag() {
        assertThat(userDTO.getUsertag(), is(nullValue()));
        assertThat(userDTO.getUsertag(), is(nullValue()));
    }

    /**
     * Test setting and getting the usertag.
     */
    @Test
    public void testSetAndGetUsertag() {
        userDTO.setUsertag("@testuser");
        userDTO.setUserStrategy("strategie");

        assertThat(userDTO.getUsertag(), is("@testuser"));
        assertThat(userDTO.getUserStrategy(), is("strategie"));
    }

    /**
     * Test setting the usertag to null.
     */
    @Test
    public void testSetUsertagToNull() {
        userDTO.setUsertag(null);
        userDTO.setUserStrategy(null);

        assertThat(userDTO.getUsertag(), is(nullValue()));
        assertThat(userDTO.getUserStrategy(), is(nullValue()));
    }
}
