package task.technical.mathematicalassistant.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import task.technical.mathematicalassistant.models.logic.EquationSolver;
import task.technical.mathematicalassistant.models.logic.EquationValidatorModel;
import task.technical.mathematicalassistant.models.logic.RootValidationModel;

@Controller
public class MainController {
    private final String ERROR_MESSAGE_FORMAT = "<span style='color: red;'>%s</span>";
    private final String SUCCESS_MESSAGE_FORMAT = "<span style='color: green;'>%s. The %s, has been successfully saved.</span>";

    @GetMapping("/")
    public String home(Model model) {
           return "home";
    }

    @PostMapping("/submit")
    public String submit(@RequestParam("equation") String equation,
                         @RequestParam("root") String root,
                         Model model) {
        String validatedEquation = "";
        String validatedRoot = "";
        if (equation.isEmpty()) {
            model.addAttribute("resultMessageEquation", String.format(ERROR_MESSAGE_FORMAT, "The equation value cannot be empty."));
        } else {
            try {
                validatedEquation = new EquationValidatorModel(equation)
                        .checkContainsOnlyValidCharacters()
                        .checkParentheses()
                        .checkParentheses()
                        .checkExpression()
                        .getEquation();
            } catch (IllegalArgumentException e) {
                model.addAttribute("resultMessageEquation", String.format(ERROR_MESSAGE_FORMAT, e.getMessage()));
            }
        }
        if (root.isEmpty()) {
            model.addAttribute("resultMessageRoot", String.format(ERROR_MESSAGE_FORMAT, "The equation value cannot be empty."));
        } else {
            try {
                validatedRoot = new RootValidationModel(root).validateRootElement().getRoot();
                } catch (IllegalArgumentException e) {
                model.addAttribute("resultMessageRoot", String.format(ERROR_MESSAGE_FORMAT, e.getMessage()));
            }
        }
        if (!validatedEquation.equals("") && !validatedRoot.equals("")) {
            EquationSolver solver = new EquationSolver();
            boolean result = solver.isRootValidForEquation(validatedEquation, validatedRoot);
            if(result) {
                model.addAttribute("resultMessage", String.format(SUCCESS_MESSAGE_FORMAT, "Root: "
                        +validatedRoot + " is a valid for provided equation","equation " + validatedEquation));
            } else {
                model.addAttribute("resultMessage", String.format(ERROR_MESSAGE_FORMAT, "Root: " + validatedEquation
                        + " is not a solution to the provided equation: "+ validatedRoot));
            }
        }
        return "home";
    }

    @GetMapping("/search")
    public String search( @RequestParam("root") String root, Model model) {
        if (root.isEmpty()) {
            model.addAttribute("resultMessageRoot", String.format(ERROR_MESSAGE_FORMAT, "The root value cannot be empty"));
        }
        try {
            String validatedRoot =  new RootValidationModel(root)
                    .validateRootElement()
                    .getRoot();
            model.addAttribute("resultMessage", String.format(SUCCESS_MESSAGE_FORMAT,"" ,"root " + validatedRoot)); // добавить логику тянуть из БД
        } catch (IllegalArgumentException e) {
            model.addAttribute("resultMessageRoot", String.format(ERROR_MESSAGE_FORMAT, e.getMessage()));
        }
        return "home";
    }
}