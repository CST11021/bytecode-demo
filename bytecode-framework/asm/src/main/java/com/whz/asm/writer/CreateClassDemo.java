package com.whz.asm.writer;

import cn.hutool.core.util.ReflectUtil;
import com.whz.asm.anotation.Czk;
import com.whz.asm.uitls.ClassPatchUtil;
import com.whz.asm.uitls.FileUtil;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintStream;
import java.io.PrintWriter;

import static org.objectweb.asm.Opcodes.*;

/**
 * 动态生成一个.class类
 *
 * @Author:ChenZhangKun
 * @Date: 2021/9/13 22:29
 */
public class CreateClassDemo {

    public static final String WHZ_CLASS_PATH = "com/whz/asmgen/WhzObject";

    public static void main(String[] args) throws Exception {


        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // 一、创建一个类
        cw.visit(V1_8, ACC_PUBLIC, WHZ_CLASS_PATH, null, "java/lang/Object", null);

            createFileWithAnnotation(cw);
            createConstructorMethod(cw);
            createStaticInitMethod(cw);
            createMethod(cw);
            createMainMethod(cw);

        cw.visitEnd();

        // 写入.class文件
        FileUtil.writeBytes(ClassPatchUtil.convertClassPath(WHZ_CLASS_PATH), cw.toByteArray());

        // 测试
        Class<?> clazz = Class.forName(ClassPatchUtil.convertClassName(WHZ_CLASS_PATH));
        Object object = clazz.newInstance();
        ReflectUtil.invoke(object, "testIf", 1);
    }

    private static void createFileWithAnnotation(ClassWriter cw) {
        {
            // ACC_PUBLIC|ACC_STATIC|ACC_FINAL 访问标识符
            // name:字段名称
            // descriptor:字段类型
            // signature:签名
            // value:默认值
            FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC | ACC_STATIC | ACC_FINAL,
                    "lover", "Ljava/lang/String;", null, "nzq");
            System.out.println(fieldVisitor.getClass());

            // 加入注解
            String classNamePath = ClassPatchUtil.convertClassNamePath(Czk.class);
            AnnotationVisitor annotationVisitor = fieldVisitor.visitAnnotation("L" + classNamePath +";", false);

            // 注解访问完毕
            annotationVisitor.visitEnd();
            fieldVisitor.visitEnd();
        }
    }

    private static void createConstructorMethod(ClassWriter cw) {
        {
            MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            // 构造方法代码
            methodVisitor.visitCode();
            // 加载localVariable slot0 this指针的位置
            methodVisitor.visitVarInsn(ALOAD, 0);
            // 执行构造方法
            // INVOKESPECIAL构造方法执行指令
            // owner:Object的类空参构造
            // name:构造方法名称
            // descriptor:构造方法描述
            // isinterface:是否是接口
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            // 返回语句
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1,1);
            methodVisitor.visitEnd();
        }
    }

    private static void createStaticInitMethod(ClassWriter cw) {
        // 创建的静态代码块
        {
            // 动态生成静态代码块
            MethodVisitor methodVisitor = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            // 开始
            methodVisitor.visitCode();
            // 生成field
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            // 加载需要打印到operate stack中的值
            methodVisitor.visitLdcInsn("czk=========");
            // INVOKEVIRTUAL 方法指令
            // java/lang/PrintStream 方法拥有者
            // println方法名称
            // (Ljava/lang/String;)方法描述
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
    }

    public static void createMethod(ClassWriter cw) {
        // 创建一个test方法，方法内部有if逻辑
        {
            // test方法
            Label elseLabel = new Label();
            Label returnLabel = new Label();
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "testIf", "(I)V", null, null);
            mv.visitCode();
            // 加载第二个变量
            mv.visitVarInsn(ILOAD, 1);
            mv.visitInsn(ICONST_1);
            // 是否不等于1就跳转
            mv.visitJumpInsn(IF_ICMPNE, elseLabel);
            // 执行if下面的的语句 这里默认是var==0如果需要var==1,需要自己在前面将1加载到localVariable中
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            // 加载常量进入operate stack
            mv.visitLdcInsn("value is 1");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            // 返回
            mv.visitJumpInsn(GOTO, returnLabel);
            // 执行不成立
            mv.visitLabel(elseLabel);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            // 加载常量进入operate stack
            mv.visitLdcInsn("value is not 1");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitLabel(returnLabel);
            mv.visitInsn(RETURN);
            // 要写，asm可以帮助计算
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
    }

    public static void createMainMethod(ClassWriter cw) {
        PrintWriter printWriter = new PrintWriter(System.out);
        TraceClassVisitor cv = new TraceClassVisitor(cw, printWriter);
        // 创建一个main方法
        {
            Method m2 = Method.getMethod("void main (String[])");
            GeneratorAdapter mg = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, m2, null, null, cv);
            mg.getStatic(Type.getType(System.class), "out", Type.getType(PrintStream.class));
            mg.push("Hello World!");
            mg.invokeVirtual(Type.getType(PrintStream.class), Method.getMethod("void println (String)"));
            mg.returnValue();
            mg.endMethod();
        }
    }

}
