echo "[speed for writing]"
echo "----------------------------------------------------"
dd if=/dev/zero bs=1024k count=100 of=test_file oflag=direct
echo ""
echo "[speed for reading]"
echo "----------------------------------------------------"
dd if=test_file of=/dev/null bs=1024k
