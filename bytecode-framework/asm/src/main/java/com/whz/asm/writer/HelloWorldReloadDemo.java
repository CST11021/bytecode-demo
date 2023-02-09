package com.whz.asm.writer;

import com.whz.asm.uitls.ClassPatchUtil;
import com.whz.asm.HelloWorld;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 生成HelloWorld
 *
 * @Author:ChenZhangKun
 * @Date: 2021/7/19 23:28
 */
public class HelloWorldReloadDemo {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        MyClassLoader myClassLoader = new MyClassLoader();
        Class<?> clazz = myClassLoader.loadClass(HelloWorld.class.getName());
        Object o = clazz.newInstance();
        System.out.println(o.getClass());
        System.out.println(o);
    }

    /**
     * 自定义类加载器加载生成的byte数组
     *
     * @Author:ChenZhangKun
     * @Date: 2021/7/19 23:25
     */
    public static class MyClassLoader extends ClassLoader {

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (HelloWorld.class.getName().equals(name)) {
                byte[] bytes = HelloWorldReloadDemo.HelloWorldDump.dump();
                Class<?> clazzz = defineClass(name, bytes, 0, bytes.length);
                return clazzz;
            }
            return null;
        }

    }

    /**
     * 使用asm生成helloworld类
     *
     * @Author:ChenZhangKun
     * @Date: 2021/7/19 23:22
     */
    public static class HelloWorldDump implements Opcodes {

        public static byte[] dump() {
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            String classNamePath = ClassPatchUtil.convertClassNamePath(HelloWorld.class);
            cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, classNamePath, null, "java/lang/Object", null);

            {
                MethodVisitor mv1 = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
                // 访问localvariableh和opreatestack
                mv1.visitCode();
                // 加载this
                mv1.visitVarInsn(ALOAD, 0);
                // 执行构造
                mv1.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
                // 返回
                mv1.visitInsn(RETURN);
                // 会自动计算，但是也要写
                mv1.visitMaxs(1, 1);
                mv1.visitEnd();
            }
            {
                MethodVisitor mv2 = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
                mv2.visitCode();
                mv2.visitLdcInsn("This is a HelloWorld object.");
                mv2.visitInsn(ARETURN);
                mv2.visitMaxs(1, 1);
                mv2.visitEnd();
            }
            cw.visitEnd();

            return cw.toByteArray();
        }
    }
}
