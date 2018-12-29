package ${basePackage}.service;
import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.entity.common.ResultData;


/**
 * Created by ${author} on ${date}.
 */
public interface ${modelNameUpperCamel}Service {

    ResultData add(${modelNameUpperCamel} obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(${modelNameUpperCamel} obj) throws Exception;

    ResultData get(Integer id) throws Exception;
}
