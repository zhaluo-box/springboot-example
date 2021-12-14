package com.example.boot.base.entity.jpa;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by MingZhe on 2021/10/15 16:16:52
 *
 * @author wmz
 */

@Entity
@Data
@Table
@Accessors(chain = true)
public class PhotoAlbum {

    @Id
    private String id;

    /**
     * 相册名称
     */
    private String Name;

    /**
     * 类别
     */
    private String category;

}
