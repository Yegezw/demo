import com.zzw.util.Util;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

public class UnsafeTest
{

    @Test
    public void memoryTest()
    {
        Unsafe unsafe = Util.getUnsafe();

        int  size  = 4;
        long addr0 = unsafe.allocateMemory(size);
        long addr3 = unsafe.reallocateMemory(addr0, size * 2);
        System.out.println("addr0: " + addr0); // 105553164728448
        System.out.println("addr3: " + addr3); // 105553164728448

        try
        {
            // 内存起始地址 addr0, 内存块大小为 size, 每个字节都设置为 1
            unsafe.setMemory(null, addr0, size, (byte) 1);
            // 内存拷贝: addr0 -> addr3、addr0 -> addr3 + 4, 每次拷贝 4 bytes
            for (int i = 0; i < 2; i++)
            {
                unsafe.copyMemory(null, addr0, null, addr3 + size * i, 4);
            }
            System.out.println(unsafe.getInt(addr0));  // 16843009
            System.out.println(unsafe.getLong(addr3)); // 72340172838076673
        }
        finally
        {
            unsafe.freeMemory(addr0); // 释放内存
            unsafe.freeMemory(addr3); // 释放内存
        }
    }
}
