package vo;

import com.itheima.core.pojo.specification.Specification;
import com.itheima.core.pojo.specification.SpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @描述: vo代表包装对象或组合对象: 即规格对象和规格选项对象的组合
 *
 * @Auther: yanlong
 * @Date: 2019/3/5 17:56:06
 * @Version: 1.0
 */
public class SpecificationVo implements Serializable {
    //规格对象  一的一方
    private Specification specification;

    //规格选项对象结果集  多的一方
    private List<SpecificationOption> specificationOptionList;

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<SpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<SpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
