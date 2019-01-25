package ${basePackage}.controller;
import ${basePackage}.entity.ResultData;
import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import ${basePackage}.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.Errors;

import javax.validation.Valid;
import javax.annotation.Resource;

/**
* Created by ${author} on ${date}.
*/
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller extends BaseController {
    private Logger logger = LoggerFactory.getLogger("${modelNameUpperCamel}Controller");

    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @RequestMapping("/add")
    public ResultData add(@Valid ${modelNameUpperCamel} ${modelNameLowerCamel}, Errors errors) {
        // 检查参数
        ResultData resultData = handleErrors(errors);
        if(resultData != null) {
            return resultData;
        }

        try {
            resultData = ${modelNameLowerCamel}Service.add(${modelNameLowerCamel});
            return resultData;
        } catch (Exception e) {
            logger.error("添加数据异常=" + e.getMessage());
            return ResultData.getErrResult("添加失败");
        }
    }

    @RequestMapping("/delete")
    public ResultData delete(@RequestParam Integer id) {
        try {
            return ${modelNameLowerCamel}Service.del(id);
        } catch (Exception e) {
            logger.error("删除数据异常=" + e.getMessage());
            return ResultData.getErrResult("删除失败");
        }
    }

    @RequestMapping("/update")
    public ResultData update(@Valid ${modelNameUpperCamel} ${modelNameLowerCamel}, Errors errors) {
        // 检查参数
        ResultData resultData = handleErrors(errors);
        if(resultData != null) {
            return resultData;
        }

        try {
            resultData = ${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
            return resultData;
        } catch (Exception e) {
            logger.error("更新数据异常=" + e.getMessage());
            return ResultData.getErrResult("更新失败");
        }
    }

    @RequestMapping("/detail")
    public ResultData detail(@RequestParam Integer id) {
        try {
            ResultData resultData = ${modelNameLowerCamel}Service.get(id);
            return resultData;
        } catch (Exception e) {
            logger.error("查询数据异常=" + e.getMessage());
            return ResultData.getErrResult("查询失败");
        }
    }

}
