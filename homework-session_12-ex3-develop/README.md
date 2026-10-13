# Bài 3: Tách biệt luồng CI/CD đa môi trường (Staging & Production) với GitHub Environments

## 1. Mục tiêu
- Quản lý hạ tầng triển khai đa môi trường (**Staging** vs **Production**) thông qua tính năng **GitHub Environments**.
- Cấu hình **Environment Protection Rules** yêu cầu phê duyệt thủ công (**Required reviewers**) trước khi deploy lên môi trường **Production**.

---

## 2. Cấu trúc thư mục dự án

```text
SESSION12_BAI3/
├── .github/
│   └── workflows/
│       └── cd-multi-env.yml      # Kịch bản CD đa môi trường (Staging & Production)
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── homework/
│   └── session_12/
│       └── ex3/                  # Thư mục sản phẩm nộp bài theo đường dẫn gợi ý
│           ├── .github/
│           │   └── workflows/
│           │       └── cd-multi-env.yml
│           └── README.md
├── src/
│   ├── main/java/com/example/
│   │   ├── Application.java
│   │   └── Calculator.java
│   └── test/java/com/example/
│       └── CalculatorTest.java
├── .dockerignore
├── .gitattributes
├── .gitignore
├── Dockerfile
├── build.gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
└── README.md
```

---

## 3. Nội dung tệp kịch bản `.github/workflows/cd-multi-env.yml`

```yaml
name: Multi-Environment CD (Staging & Production)

on:
  push:
    branches:
      - develop
      - main

jobs:
  deploy-staging:
    name: Deploy to Staging
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/develop'
    environment:
      name: staging
      url: https://api.staging.example.com

    steps:
      - name: Checkout repository
        uses: actions/checkout@v5

      - name: Deploy application to Staging
        run: |
          echo "🚀 Starting deployment to STAGING environment..."
          echo "Branch: ${{ github.ref_name }}"
          echo "Commit SHA: ${{ github.sha }}"
          echo "Target URL: https://api.staging.example.com"
          echo "✅ Successfully deployed to Staging!"

  deploy-prod:
    name: Deploy to Production
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    environment:
      name: production
      url: https://api.prod.example.com

    steps:
      - name: Checkout repository
        uses: actions/checkout@v5

      - name: Deploy application to Production
        run: |
          echo "🚀 Starting deployment to PRODUCTION environment..."
          echo "Branch: ${{ github.ref_name }}"
          echo "Commit SHA: ${{ github.sha }}"
          echo "Target URL: https://api.prod.example.com"
          echo "✅ Successfully deployed to Production!"
```

---

## 4. Hướng dẫn thiết lập GitHub Environments, kiểm tra và nộp bài

### Bước 1: Tạo GitHub Repository (Public) và cấu hình Environments
> **Lưu ý:** Tính năng **Environment protection rules (Required reviewers)** được hỗ trợ miễn phí trên **Public Repository** đối với tài khoản GitHub Free. Hãy đảm bảo tạo repository ở chế độ **Public**.

1. Trên GitHub Repository vừa tạo, truy cập **Settings** -> **Environments** (ở thanh menu bên trái).
2. Nhấn **New environment**, nhập tên **`staging`** -> nhấn **Configure environment** (môi trường `staging` không cần bật Required reviewers để tự động deploy ngay khi push vào nhánh `develop`).
3. Quay lại **Environments** -> nhấn **New environment**, nhập tên **`production`** -> nhấn **Configure environment**.
4. Tại trang cấu hình môi trường **`production`**:
   - Tích chọn ô **Required reviewers**.
   - Trong ô tìm kiếm người duyệt, nhập **username tài khoản GitHub cá nhân của bạn** và chọn tài khoản đó.
   - Nhấn nút **Save protection rules** để lưu lại.

---

### Bước 2: Đẩy mã nguồn lên nhánh `develop` (kiểm tra luồng Staging) và nhánh `main` (kiểm tra luồng Production)
Mở terminal tại thư mục `SESSION12_BAI3` và chạy lần lượt các lệnh sau:

```bash
# Khởi tạo Git và commit mã nguồn
git init
git add .
git commit -m "feat(ex3): add multi-environment CD workflow for staging and production"

# Thêm remote GitHub Repository của bạn
git remote add origin https://github.com/Ngotrungchien232/homework-session_12-ex3.git

# 1. Tạo và đẩy nhánh develop -> Kích hoạt tự động Job deploy-staging
git checkout -b develop
git push -u origin develop

# 2. Tạo và đẩy nhánh main -> Kích hoạt Job deploy-prod (sẽ dừng lại chờ phê duyệt)
git checkout -b main
git push -u origin main
```

---

### Bước 3: Kiểm tra phê duyệt Deployment và chụp ảnh màn hình nộp bài
1. Truy cập tab **Actions** trên GitHub Repository:
   - Với lần push vào nhánh `develop`: Job **Deploy to Staging (`deploy-staging`)** chạy thẳng và hoàn tất tích xanh, còn `deploy-prod` được bỏ qua (Skipped).
   - Với lần push vào nhánh `main`: Luồng chạy dừng lại tại bước **Deploy to Production (`deploy-prod`)** với trạng thái màu vàng **Waiting for review** (do quy tắc Required reviewers của môi trường `production`).
2. Nhấn vào workflow run của nhánh `main`:
   - Nhấn nút **Review deployments** ở góc phải thông báo chờ duyệt.
   - Cửa sổ phê duyệt hiện ra với tuỳ chọn tích vào môi trường **`production`** và nút màu xanh **Approve and deploy**.
   - **📸 Chụp ảnh màn hình cửa sổ phê duyệt này** và lưu vào thư mục `homework/session_12/ex3/`.
3. Tích chọn **`production`**, nhập ghi chú (tuỳ chọn) và bấm **Approve and deploy**:
   - Quan sát Job **Deploy to Production (`deploy-prod`)** tiếp tục thực thi và chuyển sang trạng thái hoàn thành **tích xanh** cùng đường dẫn `https://api.prod.example.com`.
