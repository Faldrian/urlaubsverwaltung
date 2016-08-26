package org.synyx.urlaubsverwaltung.web.settings;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.Errors;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.synyx.urlaubsverwaltung.core.mail.MailService;
import org.synyx.urlaubsverwaltung.core.period.DayLength;
import org.synyx.urlaubsverwaltung.core.settings.FederalState;
import org.synyx.urlaubsverwaltung.core.settings.Settings;
import org.synyx.urlaubsverwaltung.core.settings.SettingsService;
import org.synyx.urlaubsverwaltung.core.sync.CalendarSyncService;
import org.synyx.urlaubsverwaltung.security.SecurityRules;
import org.synyx.urlaubsverwaltung.web.ControllerConstants;


/**
 * Daniel Hammann - <hammann@synyx.de>.
 */
@Controller
@RequestMapping("/web")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private CalendarSyncService calendarSyncService;

    @Autowired
    private MailService mailService;

    @Autowired
    private SettingsValidator settingsValidator;

    @PreAuthorize(SecurityRules.IS_OFFICE)
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String settingsDetails(Model model) {

        model.addAttribute("settings", settingsService.getSettings());
        model.addAttribute("federalStateTypes", FederalState.values());
        model.addAttribute("dayLengthTypes", DayLength.values());

        return "settings/settings_form";
    }


    @PreAuthorize(SecurityRules.IS_OFFICE)
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String settingsSaved(@ModelAttribute("settings") Settings settings, Errors errors, Model model,
        RedirectAttributes redirectAttributes) {

        settingsValidator.validate(settings, errors);

        if (errors.hasErrors()) {
            model.addAttribute("settings", settings);
            model.addAttribute("federalStateTypes", FederalState.values());
            model.addAttribute("dayLengthTypes", DayLength.values());
            model.addAttribute(ControllerConstants.ERRORS_ATTRIBUTE, errors);

            return "settings/settings_form";
        }

        settingsService.save(settings.getAbsenceSettings());
        settingsService.save(settings.getWorkingTimeSettings());
        settingsService.save(settings.getMailSettings());
        settingsService.save(settings.getCalendarSettings());

        mailService.sendSuccessfullyUpdatedSettingsNotification(settings);
        calendarSyncService.checkCalendarSyncSettings();

        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/web/settings";
    }
}
