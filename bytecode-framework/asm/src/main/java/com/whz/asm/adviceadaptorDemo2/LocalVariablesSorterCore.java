package com.whz.asm.adviceadaptorDemo2;

import com.whz.asm.uitls.ClassPatchUtil;
import com.whz.asm.uitls.FileUtil;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

/**
 * 使用localVariablesStore计算时间
 *
 * @Author:ChenZhangKun
 * @Date: 2021/9/9 18:15
 */
public class LocalVariablesSorterCore {

    public static void main(String[] args) {
        String relative_path = ClassPatchUtil.convertClassPath(Person.class);
        byte[] bytes1 = FileUtil.readBytes(relative_path);

        //（1）构建ClassReader
        ClassReader cr = new ClassReader(bytes1);

        //（2）构建ClassWriter
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        //（3）串连ClassVisitor
        int api = Opcodes.ASM9;
        // 使用adviceadaptor方法其可以对构造方法进行处理
        ClassVisitor cv = new MethodTimerVisitor4(api, cw);

        //（4）结合ClassReader和ClassVisitor
        int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, parsingOptions);

        //（5）生成byte[]
        byte[] bytes2 = cw.toByteArray();

        FileUtil.writeBytes(relative_path, bytes2);
    }

    public static class MethodTimerVisitor4 extends ClassVisitor {

        public MethodTimerVisitor4(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            /**
             * 这里没有排除构造方，因为AdviceAdapter能处理构造方法
             */
            if (mv != null) {
                boolean isAbstractMethod = (access & ACC_ABSTRACT) != 0;
                boolean isNativeMethod = (access & ACC_NATIVE) != 0;
                if (!isAbstractMethod && !isNativeMethod) {
                    mv = new MethodTimerAdapter4(api, mv, access, name, descriptor);
                }
            }
            return mv;
        }

        private static class MethodTimerAdapter4 extends AdviceAdapter {
            /**  */
            private int slotIndex;

            public MethodTimerAdapter4(int api, MethodVisitor mv, int access, String name, String descriptor) {
                super(api, mv, access, name, descriptor);
            }

            @Override
            protected void onMethodEnter() {
                // 创建变量
                slotIndex = newLocal(Type.LONG_TYPE);
                // 调用System.currentTimeMillis()生成当前时间戳
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                // Opcodes.LSTORE是指令，表示存一个Long类型的变量
                mv.visitVarInsn(LSTORE, slotIndex);
            }

            @Override
            protected void onMethodExit(int opcode) {
                if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                    // 调用System.currentTimeMillis()生成当前时间戳
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                    // 求得的时间入栈
                    mv.visitVarInsn(LLOAD, slotIndex);
                    // 做减法
                    mv.visitInsn(LSUB);
                    // 减去代码执行前生成的时间戳，表示存一个Long类型的变量
                    mv.visitVarInsn(LSTORE, slotIndex);

                    // 一下代码是生成耗时的日志：System.out.println("hello()V 方法执行耗时: " + var1);
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                    mv.visitInsn(DUP);


                    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                    mv.visitLdcInsn(getName() + methodDesc + "方法执行耗时: ");
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                    mv.visitVarInsn(LLOAD, slotIndex);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

                }
            }
        }
    }

}
