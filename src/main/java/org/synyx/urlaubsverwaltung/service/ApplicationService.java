package org.synyx.urlaubsverwaltung.service;

import org.joda.time.DateMidnight;

import org.synyx.urlaubsverwaltung.domain.Application;
import org.synyx.urlaubsverwaltung.domain.ApplicationStatus;
import org.synyx.urlaubsverwaltung.domain.Person;

import java.math.BigDecimal;

import java.util.List;


/**
 * use this service to access to the application-data (who, how many days, ...)
 *
 * @author  Johannes Reuter
 * @author  Aljona Murygina
 */
public interface ApplicationService {

    /**
     * use this to get an application by its id
     *
     * @param  id
     *
     * @return
     */
    Application getApplicationById(Integer id);


    /**
     * use this to save simple an edited application (e.g. after adding sick days)
     *
     * @param  application  the application to be saved
     */
    void simpleSave(Application application);


    /**
     * use this to save a new application, i.e. state is set to waiting and calculation (subtracting vacation days from
     * holidays account) is performed
     *
     * @param  application  the application to be saved
     */
    void save(Application application);


    /**
     * use this to set a application to allowed (only boss)
     *
     * @param  application  the application to be edited
     */
    void allow(Application application, Person boss);


    /**
     * use this to set a application to rejected (only boss)
     *
     * @param  application  the application to be edited
     */
    void reject(Application application, Person boss);


    /**
     * application's state is set to cancelled if user cancels vacation
     *
     * @param  application
     */
    void cancel(Application application);


    /**
     * This method set the given sick days to person's holidays accpunt: the number of sick days is credited to the
     * holidays account, because sick days are not counted among to holidays. Explanatory note: this method doesn't add,
     * but set(!) the sick days, i.e. if the field 'sickDays' of application is already filled, it will be overwritten
     * by the new value.
     *
     * @param  application
     * @param  sickDays
     */
    void addSickDaysOnHolidaysAccount(Application application, BigDecimal sickDays);


    /**
     * use this to get all applications of a certain person
     *
     * @param  person  the person you want to get the applications of
     *
     * @return  returns all applications of a person as a list of Application-objects
     */
    List<Application> getApplicationsByPerson(Person person);


    /**
     * use this to get all applications by person and year (only not cancelled applications)
     *
     * @param  person
     * @param  year
     *
     * @return  return a list of applications of the given person and year
     */
    List<Application> getApplicationsByPersonAndYear(Person person, int year);


    /**
     * use this to get all applications by a certain state (like waiting) and year
     *
     * @param  state
     * @param  year
     *
     * @return  returns all applications of a state as a list of application-objects
     */
    List<Application> getApplicationsByStateAndYear(ApplicationStatus state, int year);


    /**
     * use this to get all applications with vacation time between startDate x and endDate y
     *
     * @param  startDate
     * @param  endDate
     *
     * @return
     */
    List<Application> getApplicationsForACertainTime(DateMidnight startDate, DateMidnight endDate);


    /**
     * signs an application with the private key of the signing user (applicant)
     *
     * @param  application
     * @param  user
     */
    void signApplicationByUser(Application application, Person user);


    /**
     * signs an application with the private key of the signing boss
     *
     * @param  application
     * @param  boss
     */
    void signApplicationByBoss(Application application, Person boss);


    /**
     * check if application is valid and may be send to boss to be allowed or rejected or if person's leave account has
     * too little residual number of vacation days, so that taking holiday isn't possible
     *
     * @param  application
     *
     * @return  boolean: true if application is okay, false if there are too little residual number of vacation days
     */
    boolean checkApplication(Application application);


    /**
     * Check if new application is overlapping with an existent application. There are three possible cases: (1) The
     * period of the new application has no overlap at all with existent applications; i.e. you can calculate the normal
     * way and save the application if there are enough vacation days on person's holidays account. (2) The period of
     * the new application is element of an existent application's period; i.e. the new application is not necessary
     * because there is already an existent application for this period. (3) The period of the new application is part
     * of an existent application's period, but for a part of it you could apply new vacation; i.e. user must be asked
     * if he wants to apply for leave for the not overlapping period of the new application.
     *
     * @param  application  (the new application)
     *
     * @return  1 for case 1, 2 for case 2, 3 for case 3
     */
    int checkOverlap(Application application);
}
