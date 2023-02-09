package com.whz.asm.writer;

import com.whz.asm.uitls.ClassPatchUtil;
import com.whz.asm.uitls.FileUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;


/**
 * @Author:ChenZhangKun
 * @Date: 2021/7/20 18:10
 */
public class HelloWorldReplaceDemo {

    public static final String HELLO_WORLD = "com/whz/asm/HelloWorld";

    public static void main(String[] args) {
        String path = ClassPatchUtil.convertClassPath(HELLO_WORLD);
        // 生成字节码
        byte[] bye = dump();
        FileUtil.writeBytes(path, bye);
    }

    private static byte[] dump() {
        // 创建写出对象
        // 两个构造方法
        // public ClassWriter(ClassReader classReader, int flags)这和构造方法可能会造成常量池的冗余
        // 可以选取三个值
        // 第一个，可以选取的值是0。ASM不会自动计算max stacks和max locals，也不会自动计算stack map frames。
        // 第二个，可以选取的值是ClassWriter.COMPUTE_MAXS。ASM会自动计算max stacks和max locals，但不会自动计算stack map frames。
        // 第三个，可以选取的值是ClassWriter.COMPUTE_FRAMES（推荐使用）。ASM会自动计算max stacks和max locals，也会自动计算stack map frames。
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        // 访问
        cw.visit(V1_8, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, HELLO_WORLD,
                null, "java/lang/Object", new String[]{"java/lang/Cloneable"});
        // 访问fileds
        {

            FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC+ACC_VOLATILE, "LESS", "I", null, -1);
            fieldVisitor.visitEnd();
        }
        {
            FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I", null, 0);
            fieldVisitor.visitEnd();
        }
        {
            FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC  + ACC_STATIC, "OBJ", "java/lang/String", null, "czk");
            fieldVisitor.visitEnd();
        }
        {
            FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I", null, 1);
            fieldVisitor.visitEnd();
        }
        {
            MethodVisitor mv1 = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I",
                    null, new String[]{"java/io/IOException"});
            mv1.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }
}
