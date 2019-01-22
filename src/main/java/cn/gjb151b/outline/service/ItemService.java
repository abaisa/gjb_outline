package cn.gjb151b.outline.service;

import cn.gjb151b.outline.action.ItemAction;
import cn.gjb151b.outline.model.ItemOperater;
import cn.gjb151b.outline.model.ManageSysOutline;
import cn.gjb151b.outline.outlineDao.ManageSysOutlineMapper;
import cn.gjb151b.outline.outlineDao.OutlineUserInfoMapper;
import cn.gjb151b.outline.utils.BaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service(value = "ItemService")
public class ItemService {
    @Resource
    private ManageSysOutlineMapper manageSysOutlineMapper;
    @Resource
    private OutlineUserInfoMapper outlineUserInfoMapper;
    @Autowired
    public ItemService(ManageSysOutlineMapper manageSysOutlineMapper,OutlineUserInfoMapper outlineUserInfoMapper){
        this.outlineUserInfoMapper = outlineUserInfoMapper;
        this.manageSysOutlineMapper = manageSysOutlineMapper;
    }
    public BaseResponse findAllItem(){
        BaseResponse findAllItemResponse = new BaseResponse();
        List<ManageSysOutline> item = manageSysOutlineMapper.findAllItem();
        List<ItemOperater> itemList  =  new ArrayList<>();
        if(item == null){
            findAllItemResponse.setStatus("error");
            findAllItemResponse.setMessage("目前没有项目");

        }else{
            for(ManageSysOutline manageSysOutline:item){
                ItemOperater itemOperater = new ItemOperater();
                String userNewString = StringUtils.join(outlineUserInfoMapper.selectNewItemOperator(manageSysOutline.getOutlineDevItemid()),",");
                String userProofreadString = StringUtils.join(outlineUserInfoMapper.selectProofreadItemOperator(manageSysOutline.getOutlineDevItemid()),",");
                String userAuditString = StringUtils.join(outlineUserInfoMapper.selectAuditItemOperator(manageSysOutline.getOutlineDevItemid()),",");
                String userAuthorize = StringUtils.join(outlineUserInfoMapper.selectAuthorizeItemOperator(manageSysOutline.getOutlineDevItemid()),",");
                itemOperater.setUserNew(userNewString);
                itemOperater.setUserProofread(userProofreadString);
                itemOperater.setUserAudit(userAuditString);
                itemOperater.setUserAuthorize(userAuthorize);
                if(itemOperater.getUserNew() == null && itemOperater.getUserProofread()  == null &&  itemOperater.getUserAudit()  == null && itemOperater.getUserAuthorize() == null){

                }else{
                    itemOperater.setItemId(manageSysOutline.getOutlineDevItemid());
                    itemOperater.setItemName(manageSysOutline.getOutlineName());
                    itemList.add(itemOperater);

                }

            }
            findAllItemResponse.setStatus("success");
            findAllItemResponse.setData(itemList);
            findAllItemResponse.setMessage("目前有项目");
        }
        return findAllItemResponse;
    }

    public List<ItemOperater> getAllItem(){
        List<ManageSysOutline> item = manageSysOutlineMapper.findAllItem();
        List<ItemOperater> itemList = new ArrayList<>();
        ItemOperater itemOperater;
        for(ManageSysOutline manageSysOutline:item){
            itemOperater = new ItemOperater();
            itemOperater.setItemName(manageSysOutline.getOutlineName());
            itemOperater.setItemId(manageSysOutline.getOutlineDevItemid());
            itemList.add(itemOperater);
        }

        return itemList;


    }

}
