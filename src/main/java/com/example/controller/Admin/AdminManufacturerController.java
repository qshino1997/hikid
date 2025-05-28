package com.example.controller.Admin;

import com.example.entity.Manufacturer;
import com.example.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/manufacturer")
public class AdminManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;

    // 1. Trang chính quản lý nhà sản xuất
    @GetMapping("")
    public String listPage() {
        return "admin.manufacturer";
    }

    // 2. AJAX load bảng
    @GetMapping("/ajax")
    public String ajaxList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        long total = manufacturerService.count(keyword);
        List<Manufacturer> list = manufacturerService.getPaged(page, size, keyword);

        model.addAttribute("manufacturers", list);
        model.addAttribute("page", page);
        model.addAttribute("pages", (int) Math.ceil((double) total / size));
        model.addAttribute("keywordDefault", keyword);
        return "admin/manufacturer-table";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        model.addAttribute("mode", "create");
        return "admin/manufacturer-detail";
    }

    @PostMapping("/save")
    public String saveManufacturer(
            @ModelAttribute("manufacturer") @Valid Manufacturer manufacturer,
            BindingResult br,
            Model model,
            RedirectAttributes ra) {

        if (br.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/manufacturer-detail";
        }

        if(manufacturerService.findByName(manufacturer.getName().trim()) != null){
            model.addAttribute("failed", "Nhà sản xuất này đã tồn tại");
            model.addAttribute("mode", "create");
            return "admin/manufacturer-detail";
        }

        manufacturerService.saveOrUpdate(manufacturer);
        ra.addFlashAttribute("success", "Thêm nhà sản xuất thành công!");
        return "redirect:/admin/manufacturer";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes ra) {

        Manufacturer m = manufacturerService.findById(id);
        if (m == null) {
            ra.addFlashAttribute("failed", "Không tìm thấy nhà sản xuất");
            return "redirect:/admin/manufacturer";
        }
        model.addAttribute("manufacturer", m);
        model.addAttribute("mode", "edit");
        return "admin/manufacturer-detail";
    }

    @PostMapping("/update")
    public String updateManufacturer(
            @ModelAttribute("manufacturer") @Valid Manufacturer manufacturer,
            BindingResult br,
            Model model,
            RedirectAttributes ra) {

        if (br.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "admin/manufacturer-detail";
        }

        if(manufacturerService.findByName(manufacturer.getName().trim()) != null){
            model.addAttribute("failed", "Nhà sản xuất này đã tồn tại");
            model.addAttribute("mode", "edit");
            return "admin/manufacturer-detail";
        }

        manufacturerService.saveOrUpdate(manufacturer);
        ra.addFlashAttribute("success", "Cập nhật nhà sản xuất thành công!");
        return "redirect:/admin/manufacturer";
    }

    @PostMapping("/{id}/delete")
    public String deleteManufacturer(
            @PathVariable Integer id,
            RedirectAttributes ra) {

        try {
            manufacturerService.deleteById(id);
            ra.addFlashAttribute("success", "Xóa nhà sản xuất thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("failed", "Xóa thất bại: " + e.getMessage());
        }
        return "redirect:/admin/manufacturer";
    }
}
