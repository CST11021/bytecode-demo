package com.whz.asm.writer;


import com.whz.asm.uitls.ClassPatchUtil;
import com.whz.asm.uitls.FileUtil;
import com.whz.asm.anotation.Czk;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * 生成接口
 *
 * @Author:ChenZhangKun
 * @Date: 2021/7/21 15:33
 */
public class GenerateInterfaceDemo {

    public static final String Generate_Cinit = "com/whz/asmgen/GenerateInterface";

    public static void main(String[] args) {
        String path = ClassPatchUtil.convertClassPath(Generate_Cinit);

        byte[] bytes = generate();
        FileUtil.writeBytes(path, bytes);
    }

    private static byte[] generate() {
        // 选择2
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        // 开始vistied
        cw.visit(
                V1_8, // 版本
                ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, // 访问标识
                "com/czk/sample/GenerateInterface", // 接口名称 采用的是internal name的形式 注意:java/lang/Object形式是全限定类名的形式
                null, // 签名，跟泛型有关
                "java/lang/Object", // 父类
                null // 父接口
        );

        {
            FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC,
                    "intValue",
                    "I",
                    null,
                    10);
            fieldVisitor.visitEnd();
        }
        {
            FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, // 访问标识
                    "s", // 字段名称
                    "Ljava/lang/String;", // 字段类型
                    null, // 方法描述符
                    "czk"); // 字段值
            fieldVisitor.visitEnd();
        }
        {
            // 生成注解
            String classNamePath = ClassPatchUtil.convertClassNamePath(Czk.class);
            cw.visitAnnotation(classNamePath, false);
            cw.visitEnd();
        }

        cw.visitEnd();
        return cw.toByteArray();
    }
}
