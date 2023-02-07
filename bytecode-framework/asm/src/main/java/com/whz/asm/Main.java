package com.whz.asm;

import org.objectweb.asm.*;

import java.io.FileInputStream;

/**
 * @Author 盖伦
 * @Date 2023/2/3
 */
public class Main {

    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("demo1/target/classes/com/whz/asm/ByteCodeDemo.class");
        ClassReader classReader = new ClassReader(ByteCodeDemo.class.getName());
        ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM7, cw) {

            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                System.out.println("field:" + name);
                return super.visitField(access, name, desc, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                System.out.println("方法" + name);
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };

        // 忽略调试信息
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
    }

}
