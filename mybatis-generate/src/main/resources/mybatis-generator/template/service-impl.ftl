package ${basePackage}.service.impl;

import ${basePackage}.service.BaseService;
import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import ${basePackage}.dao.${modelNameUpperCamel}Mapper;
import ${basePackage}.entity.common.ResultData;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * Created by ${author} on ${date}.
 */
@Service
@Transactional
public class ${modelNameUpperCamel}ServiceImpl extends BaseService<${modelNameUpperCamel}Mapper, ${modelNameUpperCamel}> implements ${modelNameUpperCamel}Service {

    @Override
    public ResultData add(${modelNameUpperCamel} obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(${modelNameUpperCamel} obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if(obj.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.updateByPrimaryKey(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        if(id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        ${modelNameUpperCamel} result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if(id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }
}
