package com.example.taskmanagement.controller;

import ch.qos.logback.core.model.Model;
import com.example.taskmanagement.api.Status;
import com.example.taskmanagement.api.TaskDto;
import com.example.taskmanagement.service.TaskService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.modelmbean.ModelMBeanConstructorInfo;
import java.util.List;
import java.util.UUID;

@Controller("/")
public class TaskController {


    private final TaskService taskService;
    private String globalStatus;
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_NO = 1;
    private Integer SELECTED_PAGE;

    @Autowired
    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public ModelAndView home(@ModelAttribute("alertMessage") @Nullable String alertMessage){
        ModelAndView mv = new ModelAndView("index");
        if(SELECTED_PAGE == null){
            SELECTED_PAGE = PAGE_NO;
        }
        ModelAndView mvaux = modelAndViewListAux(SELECTED_PAGE, mv);
        mvaux.addObject("alertMessage", alertMessage);
        SELECTED_PAGE = null;
        return mvaux;

    }

    @GetMapping("add-new-task")
    public ModelAndView pageNewTask(){
        ModelAndView mv = new ModelAndView("new-task");
        String message = "";
        return modelAndViewAux(mv, new TaskDto(), message);
    }


    @PostMapping("/add-or-update-task")
    public ModelAndView addOrUpdateTask(final @Valid TaskDto taskDto,
                                        final BindingResult bindResult,
                                        final RedirectAttributes redirectAttributes){

        String message = "Error, please fill the form correctly";


        if(bindResult.hasErrors()){
            ModelAndView mv = new ModelAndView("new-task");
            return modelAndViewAux(mv, taskDto, message);
        }

        if(taskDto.getId() == null){
            taskService.saveTask(taskDto);
            redirectAttributes.addFlashAttribute("alertMessage","New Task was been successfully saved");
        }else{
            taskService.updateTask(taskDto);
            redirectAttributes.addFlashAttribute("alertMessage","Task was been successfully updated");
        }

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/edit-task/{id}")
    public ModelAndView editTask(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes){
        TaskDto taskDto = taskService.getTaskById(id);
        redirectAttributes.addFlashAttribute("taskDto", taskDto);
        return new ModelAndView("redirect:/edit-task");
    }

    @GetMapping("/edit-task")
    public ModelAndView editTaskRedirect(Model model, @ModelAttribute("taskDto") TaskDto taskDto){
        ModelAndView mv = new ModelAndView("new-task");
        String message = "";
        return modelAndViewAux(mv, taskDto, message);
    }

    @DeleteMapping("delete-task/{id}")
    public ModelAndView deleteTask(@PathVariable UUID id){
        taskService.deleteTask(id);
        ModelAndView mv = new ModelAndView("components/task-card");
        return modelAndViewListAux(SELECTED_PAGE != null ? SELECTED_PAGE : 1, mv);
    }

    @GetMapping("task-by-status")
    public ModelAndView getTaskListByStatus(@RequestParam(name = "status", required = false) String status){
        ModelAndView mv = new ModelAndView("redirect:/");
        globalStatus = status;
        return mv;
    }

    @GetMapping("/page/{pageNo}")
    public ModelAndView findPaginated(@PathVariable(value = "pageNo") int pageNo){

        ModelAndView mv = new ModelAndView("components/task-card");
        SELECTED_PAGE = pageNo;
        return modelAndViewListAux(pageNo, mv);

    }

    public ModelAndView modelAndViewListAux(int selectedPage, ModelAndView mv){

        Page<TaskDto> page = taskService.getTaskListPaginated(selectedPage, PAGE_SIZE, globalStatus);
        List<TaskDto> taskDtoList = page.getContent();
        mv.addObject("taskDtoList", taskDtoList);
        mv.addObject("currentPage", selectedPage);
        mv.addObject("totalPages", page.getTotalPages());
        mv.addObject("totalItens", page.getTotalElements());
        return mv;
    }

    public ModelAndView modelAndViewAux(ModelAndView mv, TaskDto taskDto, String message){
        mv.addObject("taskDto", taskDto);
        mv.addObject("priorities",taskService.getPriorities());
        mv.addObject("statusList",taskService.getStatus());
        mv.addObject("alertMessage", message);
        return mv;
    }

}
