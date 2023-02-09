package com.whz.asm.adviceadaptorDemo1;

import com.whz.asm.uitls.ClassPatchUtil;
import com.whz.asm.uitls.FileUtil;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

/**
 * @Author:ChenZhangKun
 * @Date: 2021/9/8 17:47
 */
public class AdviceAdaptorTransformDemo {

    public static final String ParameterUtilsNamePath = ClassPatchUtil.convertClassNamePath(ParameterUtils.class);

    public static void main(String[] args) {

        String relative_path = ClassPatchUtil.convertClassPath(Sample.class);

        //（1）构建ClassReader
        ClassReader cr = new ClassReader(FileUtil.readBytes(relative_path));

        //（2）构建ClassWriter
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        //（3）串连ClassVisitor
        ClassVisitor cv = new ClassPrintParameterVisitor(Opcodes.ASM9, cw);

        //（4）结合ClassReader和ClassVisitor
        cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        //（5）生成byte[]
        FileUtil.writeBytes(relative_path, cw.toByteArray());
    }

    public static class ClassPrintParameterVisitor extends ClassVisitor {

        public ClassPrintParameterVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (mv != null) {
                boolean isAbstractMethod = (access & ACC_ABSTRACT) != 0;
                boolean isNativeMethod = (access & ACC_NATIVE) != 0;
                if (!isAbstractMethod && !isNativeMethod) {
                    mv = new MethodPrintParameterAdapter(api, mv, access, name, descriptor);
                }
            }
            return mv;
        }

    }

    public static class MethodPrintParameterAdapter extends AdviceAdapter {

        public MethodPrintParameterAdapter(int api, MethodVisitor mv, int access, String name, String descriptor) {
            super(api, mv, access, name, descriptor);
        }

        @Override
        protected void onMethodEnter() {
            printString("进入方法: " + getName() + methodDesc);

            Type[] argumentTypes = getArgumentTypes();
            for (int i = 0; i < argumentTypes.length; i++) {
                Type t = argumentTypes[i];
                // 生成将给定方法参数加载到堆栈上的指令
                loadArg(i);
                // 生成对堆栈顶部值进行装箱的指令。 此值将被堆栈顶部的盒装等效值替换。
                box(t);
                printObject();
            }
        }

        /**
         * 方法返回指令 （ireturn 、lreturn、freturn、dreturn、areturn、return）
         * ireturn——从方法中返回int类型的数据
         * lreturn——从方法中返回long类型的数据
         * freturn——从方法中返回float类型的数据
         * dreturn—— 从方法中返回double类型的数据
         * areturn——从方法中返回引用类型的数据
         * return—— 从方法中返回，返回值为void
         *
         * @param opcode 可能的值如下：
         *              {@link Opcodes#RETURN},
         *              {@link Opcodes#IRETURN},
         *              {@link Opcodes#FRETURN},
         *              {@link Opcodes#ARETURN},
         *              {@link Opcodes#LRETURN},
         *              {@link Opcodes#DRETURN},
         *              {@link Opcodes#ATHROW}.
         */
        @Override
        protected void onMethodExit(int opcode) {
            printString("退出方法: " + getName() + methodDesc);

            if (opcode == ATHROW) {
                super.visitLdcInsn("异常返回");
            } else if (opcode == RETURN) {
                super.visitLdcInsn("void返回");
            } else if (opcode == ARETURN) {
                // 生成一条DUP指令
                dup();
            } else {
                if (opcode == LRETURN || opcode == DRETURN) {
                    dup2();
                } else {
                    dup();
                }
                // 对返回的类型进行装箱
                box(getReturnType());
            }

            printObject();
        }

        /**
         * 打印字符串
         *
         * @param str
         */
        private void printString(String str) {
            super.visitLdcInsn(str);
            super.visitMethodInsn(INVOKESTATIC, ParameterUtilsNamePath,
                    "printText", "(Ljava/lang/String;)V", false);
        }

        /**
         * 打印对象
         */
        private void printObject() {
            super.visitMethodInsn(INVOKESTATIC, ParameterUtilsNamePath,
                    "printValueOnStack", "(Ljava/lang/Object;)V", false);
        }
    }

}
