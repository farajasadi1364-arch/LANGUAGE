# Language OS — Android Starter (تکمیل‌شده برای Build ابری)

این نسخه نسبت به قبل تکمیل شده: `build.gradle.kts`, `settings.gradle.kts`,
`AndroidManifest.xml`, ریسورس‌های پایه (آیکون/نام اپ) و یک ورک‌فلوی
GitHub Actions (`.github/workflows/build-apk.yml`) اضافه شده که APK رو
روی سرورهای گیت‌هاب می‌سازه — یعنی نیازی به کامپیوتر یا Android Studio نیست.

⚠️ این پروژه اینجا (بدون اینترنت) کامپایل نشده؛ ممکنه در اولین Build یکی دو
تا نسخهٔ Dependency نیاز به اصلاح جزئی داشته باشه. اگه خطا گرفتی، لاگ Actions
رو برام بفرست تا درستش کنم.

## مراحل (فقط با گوشی)

### ۱. ساخت ریپازیتوری روی گیت‌هاب
- یک اکانت رایگان روی github.com بساز (اگه نداری).
- یک ریپازیتوری خالی جدید بساز (Public یا Private، فرقی نداره)، مثلاً به اسم `language-os`.

### ۲. آپلود کد از گوشی
ساده‌ترین راه نصب اپ **Termux** از F-Droid هست (نه از Play Store، نسخهٔ Play قدیمیه):

```
pkg install git -y
```

بعد فایل‌های این پروژه رو (که برات فرستادم) توی گوشی extract کن و توی Termux:

```
cd /path/to/LanguageOS-android-starter
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/USERNAME/language-os.git
git push -u origin main
```

موقع `push`، بجای پسورد باید یک **Personal Access Token** بسازی:
GitHub → Settings → Developer settings → Personal access tokens → Generate new token
(دسترسی `repo` کافیه) و همون رو بجای پسورد وارد کن.

### ۳. ساخت خودکار APK
به محض `push`، ورک‌فلوی Actions خودکار اجرا می‌شه. برای دیدنش:
- توی ریپازیتوری → تب **Actions** → اجرای در حال انجام رو باز کن.
- بعد از اتمام (چند دقیقه)، پایین صفحه بخش **Artifacts** یک فایل
  به اسم `LanguageOS-debug-apk` هست — همون فایل APK قابل نصبه.

می‌تونی از همون تب Actions، دکمهٔ **Run workflow** رو هم بزنی تا هر وقت
خواستی دوباره Build بگیری، بدون نیاز به push جدید.

### نصب APK روی گوشی
چون از گوگل‌پلی نیست، اول باید توی تنظیمات گوشی «نصب از منابع ناشناس» رو
برای مرورگر/فایل‌منیجرت فعال کنی، بعد فایل APK دانلودشده رو باز کنی.

## اگه Termux سخته
یک جایگزین: از مرورگر گوشی وارد صفحهٔ ریپازیتوری خالی روی گیت‌هاب شو →
**Add file → Upload files** → پوشهٔ پروژه رو (با زیرپوشه‌هاش) بکش و رها کن.
این روش گاهی توی مرورگر موبایل ساختار پوشه‌ها رو درست حفظ نمی‌کنه، پس اگه
جواب نداد همون روش Termux مطمئن‌تره.

## قدم بعدی
بعد از اینکه این نسخه Build گرفت و روی گوشی نصب شد، طبق فازبندی PRD
(بخش ۱۲) قدم بعدی: Auth واقعی + Placement Test، و موازی باهاش شروع بک‌اند
(بخش ۱۱ PRD — NestJS/Prisma پیشنهاد شده).
