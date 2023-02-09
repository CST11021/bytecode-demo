package com.whz.asm.uitls;

/**
 * @Author 盖伦
 * @Date 2023/2/8
 */
public class ClassPatchUtil {

    /** 通过asm生成的类路径 */
    private static final String BASE_PATCH = "bytecode-framework/asm/target/classes/";
    private static final String CLASS = ".class";

    /**
     * 例如：
     * 将：com.czk.sample.GenerateSwitch
     * 转为：com/czk/sample/GenerateSwitch
     *
     * @param clazz
     * @return
     */
    public static String convertClassNamePath(Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }

    /**
     * 例如：
     * 将：com/czk/sample/GenerateSwitch
     * 转为：com.czk.sample.GenerateSwitch
     *
     * @param classPatch
     * @return
     */
    public static String convertClassName(String classPatch) {
        return classPatch.replace('/', '.');
    }

    /**
     * 例如返回：bytecode-framework/asm/target/classes/com/whz/asmgen/GenerateIf.class
     *
     * @param classPath
     * @return
     */
    public static String convertClassPath(String classPath) {
        return BASE_PATCH + classPath + CLASS;
    }

    /**
     * 例如返回：bytecode-framework/asm/target/classes/com/whz/asmgen/GenerateIf.class
     *
     * @param clazz
     * @return
     */
    public static String convertClassPath(Class<?> clazz) {
        String path = clazz.getName().replace('.', '/');
        return new StringBuffer(BASE_PATCH)
                .append(path)
                .append(CLASS)
                .toString();
    }

}
