# Wealth - 个人理财管理 Android 应用

一款基于 Android 平台的个人理财管理应用，提供收支记账、财务统计、实时行情、基金数据、财经资讯等功能。

## 功能特性

- **收支记账**：支持记录收入与支出，涵盖餐饮、购物、交通、娱乐、医疗等多种分类，数据本地 SQLite 存储
- **财务统计**：按月/年统计收支情况，支持分类占比分析与可视化进度条展示
- **实时行情**：通过百度金融 API 获取股票涨跌幅、市值、成交量等实时排名数据
- **基金数据**：接入东方财富 API，支持股票型、混合型、债券型、指数型、QDII、FOF 等基金分类排行
- **财经资讯**：获取百度金融财经快讯，了解市场动态
- **社区互动**：本地评论系统，支持发帖与回复
- **用户管理**：注册、登录、头像上传、密码修改（本地存储）

## 技术栈

| 类别 | 技术 |
|------|------|
| 开发语言 | Java |
| 构建工具 | Gradle (AGP 7.2.2) |
| 最低支持 | Android 6.0 (API 23) |
| UI 框架 | AndroidX + ViewBinding + Material Design |
| 网络请求 | OkHttp 3 |
| 图片加载 | Glide 4.13 |
| JSON 解析 | Gson 2.8.9 |
| 下拉刷新 | SmartRefreshLayout |
| 本地存储 | SQLite (SQLiteOpenHelper) |
| 图片选择 | PictureSelector v3.11 |
| 权限管理 | EasyPermissions 3.0 |

## 项目结构

```
app/src/main/java/com/android/wealth/
├── activity/          # Activity 页面（启动、登录、注册、记账、统计等）
├── fragment/          # Fragment（首页、产品、社区、个人中心等）
├── adapter/           # RecyclerView 适配器
├── bean/              # 数据实体类
├── database/          # SQLite 数据库操作（用户、账单、社区）
├── data/              # 数据管理（用户状态、交易分类）
├── http/              # 网络请求封装（OkHttp 工具类）
├── base/              # 基类（Activity/Fragment + ViewBinding 封装）
├── utils/             # 工具类（验证码、图片加载、状态栏等）
├── view/              # 自定义 View（圆形头像、横向滚动等）
└── widget/            # 自定义组件（加载动画等）
```

## 构建与运行

1. 克隆项目到本地
2. 使用 Android Studio 打开项目
3. 生成签名文件：
   ```bash
   keytool -genkey -v -keystore app/key.jks -keyalg RSA -keysize 2048 -validity 10000
   ```
4. 配置签名信息：在 `local.properties` 中添加以下配置（已配置 .gitignore 忽略）：
   ```properties
   KEYSTORE_PASSWORD=your_password
   KEY_ALIAS=your_alias
   KEY_PASSWORD=your_key_password
   ```
5. 同步 Gradle 依赖
6. 连接设备或启动模拟器，运行项目

## 安全说明

- 签名文件 (`*.jks`, `*.keystore`) 和本地配置 (`local.properties`) 已通过 `.gitignore` 排除
- 用户密码使用本地 SQLite 存储，仅适用于学习演示场景
- API 请求使用 HTTPS 协议
- **请勿将本项目用于生产环境，仅供学习交流使用**

## 待优化项

- 实现密码加密存储（如 BCrypt）
- 完善 SSL 证书验证机制
- 升级 targetSdk 版本
- 启用代码混淆（ProGuard/R8）
- 优化数据库操作的资源管理

## 项目说明

- 所有用户数据存储在本地 SQLite 数据库，无后端服务器
- 行情与基金数据来自百度金融和东方财富公开 API
- 本项目仅供学习交流使用，请勿用于商业用途
