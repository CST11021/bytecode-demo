package com.whz.asm.writer;

import cn.hutool.core.util.ReflectUtil;
import com.whz.asm.uitls.ClassPatchUtil;
import com.whz.asm.uitls.FileUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * 测试创建带有switch逻辑的test方法
 *
 * @Author:ChenZhangKun
 * @Date: 2021/9/22 16:13
 */
public class GenerateSwitchDemo {

    public static final String Generate_Switch = "com/whz/asmgen/GenerateSwitch";

    public static void main(String[] args) throws Exception {
        // 地址
        String path = ClassPatchUtil.convertClassPath(Generate_Switch);
        // 生成的字节码字节流
        byte[] bytes = generateSwitch();
        FileUtil.writeBytes(path, bytes);

        Class<?> clazz = Class.forName(ClassPatchUtil.convertClassName(Generate_Switch));
        Object object = clazz.newInstance();
        ReflectUtil.invoke(object, "test", 1);
    }

    private static byte[] generateSwitch() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_8, ACC_PUBLIC, Generate_Switch, null, "java/lang/Object", null);
        // 构造
        {
            MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            // 开始访问代码
            methodVisitor.visitCode();
            // 加载第一个this
            methodVisitor.visitVarInsn(ALOAD, 0);
            // 执行构造
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            // 返回
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(0, 0);
            // 结束
            methodVisitor.visitEnd();
        }

        // 创建带有switch逻辑的test方法
        {
            // 生成switch语句
            MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "test", "(I)V", null, null);
            // label
            Label caseLabel1 = new Label();
            Label caseLabel2 = new Label();
            Label caseLabel3 = new Label();
            Label caseLabel4 = new Label();
            Label defaultLabel = new Label();
            Label returnLabel = new Label();
            methodVisitor.visitCode();
            // 加载方法参数
            methodVisitor.visitVarInsn(ALOAD, 1);
            // 加载label
            methodVisitor.visitTableSwitchInsn(1, 4, defaultLabel, new Label[]{caseLabel1, caseLabel2, caseLabel3, caseLabel4});
            // 开始第一段
            methodVisitor.visitLabel(caseLabel1);
            // 局部变量
            methodVisitor.visitFieldInsn(GETSTATIC, "java/io/System", "out", "Ljava/io/PrintStream;");
            // 打印
            methodVisitor.visitLdcInsn("var = 1");
            // 执行方法
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            methodVisitor.visitJumpInsn(GOTO, returnLabel);
            // 第3段
            methodVisitor.visitLabel(caseLabel2);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("val = 2");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            methodVisitor.visitJumpInsn(GOTO, returnLabel);

            // 第4段
            methodVisitor.visitLabel(caseLabel3);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("val = 3");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            methodVisitor.visitJumpInsn(GOTO, returnLabel);

            // 第5段
            methodVisitor.visitLabel(caseLabel4);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("val = 4");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            methodVisitor.visitJumpInsn(GOTO, returnLabel);

            // 第6段
            methodVisitor.visitLabel(defaultLabel);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("val is unknown");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            // 第7段
            methodVisitor.visitLabel(returnLabel);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(0, 0);
            methodVisitor.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }
}
